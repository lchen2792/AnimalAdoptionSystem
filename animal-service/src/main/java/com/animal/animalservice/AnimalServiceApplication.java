package com.animal.animalservice;

import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableMongoAuditing
public class AnimalServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AnimalServiceApplication.class, args);
	}

	@Autowired
	private AnimalProfileRepository animalProfileRepository;

	@Override
	public void run(String... args) throws Exception {
		//todo: remove after testing
//		EasyRandom random = new EasyRandom();
//		List<AnimalProfile> animalProfiles = random.objects(AnimalProfile.class, 5).toList();
//		animalProfiles.forEach(animalProfile -> {
//			animalProfile.setVersion(null);
//			animalProfile.setCreatedBy(null);
//			animalProfile.setCreatedDate(null);
//			animalProfile.setLastModifiedDate(null);
//		});
//		animalProfileRepository.saveAll(animalProfiles);
	}
}
