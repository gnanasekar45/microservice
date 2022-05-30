package com.luminn.firebase.service;


import com.luminn.firebase.dto.SupplierDTO;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.SupplierRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SupplierService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SupplierRepository supplierRepository;


	public MongoCollection getCollection(){
		MongoCollection collection = mongoTemplate.getCollection("supplier");
		return collection;
	}

	public void addUserOne(SupplierDTO supplierDTO){


			//get user id and save it here
		    //https://stackoverflow.com/questions/25485882/mongodb-one-to-many-relationship

		    /*String json = "{'name':'" + supplier.getName().toString() + "'," +
					"'licenseNumber':'" + supplier.getLicenseNumber().toString() + "'," +
					 "'userId':'[ObjectId(" + supplier.getIds().toString() + ")]'" +
					// "'userId':'[" + new ObjectId( supplier.getIds()  ) + "]'" +
					//"'$id':'ObjectId(" + supplier.getIds().toString() + ")'" +
					"}" ;*/

		Document document = new Document("name", supplierDTO.getName())
				.append("licenseNumber", supplierDTO.getLicenseNumber())
				.append("userId", new ObjectId(supplierDTO.getIds().toString()));

		//collection.insertOne(document);
		Supplier suppliers = new Supplier();
		suppliers = Converter.dtoToEntity(supplierDTO,suppliers);
		supplierRepository.save(suppliers);
		//collection.insertOne(Document.parse(json));
	}

	public Document findId(String id) {

		MongoCollection  collection = getCollection();
		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put("_id", new ObjectId(id));

		FindIterable<Document> findIterable = collection.find(queryObj);

		if (findIterable == null) {
			System.out.println(" NO DATA ");
		}

		MongoCursor<Document> cursor = findIterable.iterator();
		if (cursor.hasNext()) {
			Document document = cursor.next();
			//return document.getString("identifier");
			System.out.println(" DATA " + cursor.toString() +  document.get("name") );
			return document;
		} else {
			System.out.println(" NO DATA 2 ");
			return null;
		}
	}

	public Supplier findById(String id) {

		MongoCollection  collection = getCollection();

		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put("_id", new ObjectId(id));

		FindIterable<Document> findIterable = mongoTemplate.getCollection("supplier").find(queryObj);

		if (findIterable == null) {
			System.out.println(" NO DATA ");
		}

		for(Document doc : findIterable){
			Supplier sup = Converter.dtoToEntity(doc);
			return sup;
		}
		return null;
	}

	public Document findUserId(String id) {



		System.out.println(" id-->_ " + id);

		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put("userIds", new ObjectId(id));

		FindIterable<Document> findIterable = mongoTemplate.getCollection("supplier").find(queryObj);

		if (findIterable == null) {
			System.out.println(" NO DATA ");
		}

		MongoCursor<Document> cursor = findIterable.iterator();
		if (cursor.hasNext()) {
			Document document = cursor.next();
			//return document.getString("identifier");
			System.out.println(" DATA " + cursor.toString() +  document.get("name") );
			return document;
		} else {
			System.out.println(" NO DATA 2 ");
			return null;

		}
	}
	public String findDomainName(String id){
		Document doc= findUserId(id);
		if(doc.get("name") != null) {
			return doc.get("name").toString();
		}
		else
			return "NODOMAIN";
	}

	public Document findUser(String id) {

		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put("userIds", new ObjectId(id));

		FindIterable<Document> findIterable = mongoTemplate.getCollection("supplier").find(queryObj);

		if (findIterable == null) {
			System.out.println(" NO DATA ");
		}

		MongoCursor<Document> cursor = findIterable.iterator();
		if (cursor.hasNext()) {
			Document document = cursor.next();
			//return document.getString("identifier");
			System.out.println(" DATA " + cursor.toString() +  document.get("name") );
			return document;
		} else {
			System.out.println(" NO DATA 2 ");
			return null;

		}
	}

	public void update(String userId, String supplierId){


			MongoCollection  collection = getCollection();
			Document doc = findId(supplierId);


		BasicDBObject updateFields = new BasicDBObject();
		updateFields.append("userIds", new ObjectId(userId));
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);

		BasicDBObject searchQuery = new BasicDBObject("_id", new ObjectId(supplierId));
		if (searchQuery != null) {
			mongoTemplate.getCollection("supplier").updateOne(searchQuery, setQuery);
		}

		System.out.println(" -- 41 updateTaxiId amd taxiId --");


	}

}
