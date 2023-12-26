package com.openclassrooms.ChaTopAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.RentalsResponse;
import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.repository.MessageRepository;
import com.openclassrooms.ChaTopAPI.repository.RentalRepository;
import com.openclassrooms.ChaTopAPI.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private S3FileService s3FileService;

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    /**
     * get rental by Id
     * 
     * @param id
     * @return rental
     */
    public Rental getRental(final Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    /**
     * get all rentals
     * 
     * @return rentals
     */
    public RentalsResponse getRentals() {
        return new RentalsResponse(rentalRepository.findAll());
    }

    /**
     * delete rental
     * 
     * @param id
     */
    public String deleteRental(final Long id, User user) {
        try {

            Rental rentalToDelete = getRental(id);

            if (rentalToDelete.getId().equals(user.getId())) {
                String url = rentalToDelete.getPicture();
                deleteFile(url.substring(url.lastIndexOf('/') + 1));
                messageRepository.deleteMessageByRentalId(id);
                rentalRepository.deleteById(id);
                return "Rental and related Messages are deleted";
            } else {
                return "user not authorized to delete";
            }
        } catch (NoSuchElementException e) {
            return "Rental not found";
        }

    }

    /**
     * save rental to entity
     * 
     * @param rentalDto
     * @param user
     */
    public void saveRental(RentalDto rentalDto, User user) {
        Rental rental = dtoToEntity(rentalDto);
        rental.setOwnerId(user.getId());

        String pictureUrl = getUrlFile(rentalDto.getPicture());
        rental.setPicture(pictureUrl);
        rentalRepository.save(rental);
    }

    /**
     * Update rental
     * 
     * @param rentalDto
     */
    public void updateRental(RentalDto rentalDto) {
        try {
            Rental rentalToUpdate = getRental(rentalDto.getId());
            rentalToUpdate.setName(rentalDto.getName());
            rentalToUpdate.setPrice(rentalDto.getPrice());
            rentalToUpdate.setSurface(rentalDto.getSurface());
            rentalToUpdate.setDescription(rentalDto.getDescription());
            rentalRepository.save(rentalToUpdate);
        } catch (NoSuchElementException e) {
            logger.error("Rental not found", e);
        }
    }

    /**
     * delete file on amazon S3
     * 
     * @param fileName
     * @return ack delete message
     */
    private String deleteFile(final String fileName) {
        s3FileService.deleteObject(s3FileService.getS3BucketName(), fileName);
        return "Deleted File: " + fileName;
    }

    /**
     * upload file to amazon S3
     * 
     * @param multipartFile
     * @return file url
     */
    private String getUrlFile(MultipartFile multipartFile) {
        String urlPicture = "";

        try {
            Instant now = Instant.now();
            File file = convertMultiPartToFile(multipartFile, "");
            String fileName = now.toEpochMilli() + file.getName();

            try {
                if (isValideFile(fileName)) {
                    urlPicture = s3FileService.uploadObject(
                            s3FileService.getS3BucketName(),
                            fileName,
                            file);
                    file.delete();
                }
            } catch (SdkClientException amz) {
                // future upgrade to resend file
                urlPicture = file.getAbsolutePath();
                logger.error("Amazon error : " + amz.getMessage());
            }
        } catch (IOException e) {
            logger.error("File error : " + e.getMessage());
        } finally {
            return urlPicture;
        }
    }

    /**
     * Convert image to file
     * 
     * @param file
     * @param pathStore
     * @return converted file
     * @throws IOException
     */
    private File convertMultiPartToFile(MultipartFile file, String pathStore) throws IOException {
        File convFile = new File(pathStore + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Check extension
     * 
     * @param fileName
     * @return true if file is png/jpg/jpeg
     */
    private Boolean isValideFile(String fileName) {
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));
        return allowedFileExtensions.contains(FilenameUtils.getExtension(fileName));
    }

    /**
     * Converts a Rental dto to his entity.
     *
     * @param message The Message entity to be converted.
     * @return The Dto representation of the Message entity.
     */
    private Rental dtoToEntity(RentalDto rentalDto) {
        return modelMapper.map(rentalDto, Rental.class);
    }
}
