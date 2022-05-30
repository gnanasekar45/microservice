package com.luminn.driver;

import com.luminn.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.extern.log4j.Log4j2;

@EnableMongoRepositories(basePackageClasses = DriverRepository.class)
//@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserRepository.class))
@SpringBootApplication
@Log4j2
public class DriverServiceApplication {



	//for test
	@Value("${firebase.service}")
	private String firebaseUrl;
	@Value("${spring.data.mongodb.uri}")
	private String uri;
	@Value("${spring.data.mongodb.username}")
	private String username;
	@Value("${spring.data.mongodb.password}")
	private String password;
	@Value("${spring.data.mongodb.database}")
	private String database;
	
	public static void main(String[] args) {
		SpringApplication.run(DriverServiceApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public MongoClient getMongoClient() {
		//log.info(firebaseUrl);

		// * additional config,perhaps it will help you someday
	/*	MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .retryWrites(true)
                .applyToConnectionPoolSettings(builder ->
                        builder.maxConnectionIdleTime(5000, TimeUnit.MILLISECONDS))
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyToClusterSettings(builder -> {
                    builder.hosts(Arrays.asList(
                            new ServerAddress("cluster0-shard-00-00.eoggx.gcp.mongodb.net", 27017),
                            new ServerAddress("cluster0-shard-00-01.eoggx.gcp.mongodb.net", 27017),
                            new ServerAddress("cluster0-shard-00-02.eoggx.gcp.mongodb.net", 27017)
                    ));
                    builder.requiredReplicaSetName("Cluster0-shard-0");
                })
                .build(); */

        return MongoClients.create(uri);
	}
}
