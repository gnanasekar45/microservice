package com.luminn.firebase;

import com.luminn.firebase.model.UserModel;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
@PropertySource({
		"classpath:kvu.properties"
})
@EnableScheduling
public class FirebaseServiceApplication {

	@Value("${spring.data.mongodb.uri}")
	private String uri;
	@Value("${spring.data.mongodb.username}")
	private String username;
	@Value("${spring.data.mongodb.password}")
	private String password;
	@Value("${spring.data.mongodb.database}")
	private String database;

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(FirebaseServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(FirebaseServiceApplication.class, args);
		//SpringApplication.run(SchedulingTasksApplication.class);

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

		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE);

		return MongoClients.create(uri);
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");
		source.setCacheSeconds(3600); // Refresh cache once per hour.
		return source;
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		//freeMarkerConfigurer.setTemplateLoaderPath("/resources/email_templates");
		return freeMarkerConfigurer;
	}
	/*@Bean public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();

		//cfg.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:resources/templates"));
		//cfg.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:resources/templates"));
		//freeMarkerConfigurer.setTemplateLoaderPath("classpath:resources/email_templates");
		freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		return freeMarkerConfigurer;
	}*/

		//defines the classpath location of the freemarker templates
		//freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		// Default encoding of the template files return freeMarkerConfigurer;

	//@Primary
	//@Bean("sslContext")
	//@conditionalOnMissingcalss("")
	@Bean
	public CommandLineRunner commandLinerRunner(ApplicationContext ctx){
		return args -> {
			log.info(" ",String.join(""));
			Stream.of(ctx.getBeanDefinitionNames())
					.sorted()
					.map(beanName -> beanName + ":" + ctx.getBean(beanName).getClass().getName() )
					.filter(beanName -> beanName.contains("com.luminn.firebase"))
					.forEach(beanName -> {
						log.debug("beanName ", beanName);
					});
			};

	}

}
