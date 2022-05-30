package com.luminn.firebase.mapper;

public class TaxiCompany {

    /*
      //List<> s = new ArrayList<>();
        float[] latlong = new float[2];
        TaxiCompanyDTO taxi = new TaxiCompanyDTO();
        taxi.setId(taxiDoc.get("_id").toString());

        if (taxiDoc.get("type") != null)
            taxi.setCarType(taxiDoc.get("type").toString());
        if (taxiDoc.get("category") != null)
            taxi.setCategory(taxiDoc.get("category").toString());


        if (taxiDoc.get("status") != null)
            taxi.setStatus(taxiDoc.get("status").toString());

        if (taxiDoc.get("name") != null)
            taxi.setName(taxiDoc.get("name").toString());




        if (taxiDoc.get("location") != null) {

            //FindIterable<Document>
            //Document d =
            Document d = (Document) taxiDoc.get("location");
            d.get("coordinates");

            final ArrayList<?> jsonArray = new Gson().fromJson(d.get("coordinates").toString(), ArrayList.class);

            Iterator<?> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {

                String latlomg = iterator.next().toString();
                System.out.println(latlomg);
                latlong[i] = Float.valueOf(latlomg);
                System.out.println(" lat or long --->" + latlong[i]);
                i++;
            }
        }

        taxi.setLongitude(latlong[0]);
        taxi.setLatitude(latlong[1]);

        if (taxiDoc.get("lastUpdate") != null) {
            System.out.println(" lastUpdate --->" + taxiDoc.get("lastUpdate"));
        }
        return taxi;
     */

}
