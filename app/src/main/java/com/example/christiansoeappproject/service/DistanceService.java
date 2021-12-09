package com.example.christiansoeappproject.service;

public class DistanceService {

    private final double LAT_ENDPOINT = 55.32064;
    private final double LONG_ENDPOINT = 15.18629;

    public double toRadians(double val)
    {
        return (Math.PI / 180) * val;
    }

    //GET distance in km
    public double distanceToFerry(double lat2, double lon2)
    {
        double lon1 = toRadians(LONG_ENDPOINT);
        lon2 = toRadians(lon2);
        double lat1 = toRadians(LAT_ENDPOINT);
        lat2 = toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers
        double r = 6371;

        return (c * r);
    }
}
