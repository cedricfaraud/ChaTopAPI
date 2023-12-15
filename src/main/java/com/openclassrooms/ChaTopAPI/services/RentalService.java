package com.openclassrooms.ChaTopAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.model.User;
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
    private ModelMapper modelMapper;
    @Autowired
    private S3FileService s3FileService;

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental saveRental(RentalDto rentalDto, User user) {
        Rental rental = dtoToEntity(rentalDto);
        rental.setOwnerId(user.getId());

        String pictureUrl = getUrlFile(rentalDto.getPicture());
        rental.setPicture(pictureUrl);
        rental.setCreatedAt(Timestamp.from(Instant.now()));
        rental.setUpdatedAt(Timestamp.from(Instant.now()));
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    /* delete file on S3 */
    private String deleteFile(final String fileName) {
        s3FileService.deleteObject(s3FileService.getS3BucketName(), fileName);
        return "Deleted File: " + fileName;
    }

    /* upload file to S3 */
    private String getUrlFile(MultipartFile multipartFile) {
        try {
            Instant now = Instant.now();
            File file = convertMultiPartToFile(multipartFile, "");
            String fileName = now.toEpochMilli() + file.getName();
            if (isValideFile(fileName)) {
                String urlPicture = s3FileService.uploadObject(
                        s3FileService.getS3BucketName(),
                        fileName,
                        file);
                file.delete();
                return urlPicture;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* put file on S3 bucket */
    private File convertMultiPartToFile(MultipartFile file, String pathStore) throws IOException {
        File convFile = new File(pathStore + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /* check if valid extension */
    private Boolean isValideFile(String fileName) {
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));
        return allowedFileExtensions.contains(FilenameUtils.getExtension(fileName));
    }

    /**
     * Converts a Message entity to his dto.
     *
     * @param message The Message entity to be converted.
     * @return The Dto representation of the Message entity.
     */
    private Rental dtoToEntity(RentalDto rentalDto) {
        return modelMapper.map(rentalDto, Rental.class);
    }
}
