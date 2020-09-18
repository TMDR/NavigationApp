package com.student.navigator;


public class waypointmaths {
    public static float distance(float lon1, float lat1, float lon2, float lat2){
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344; // to kilometer
            return (float)(dist);
        }
    }
    public static float bearing(float lat, float lon, float lat2, float lon2) {
        float teta1 = (float) Math.toRadians(lat);
        float teta2 = (float) Math.toRadians(lat2);
        float delta2 = (float) Math.toRadians(lon2 - lon);
        float y = (float)Math.sin(delta2) * (float)Math.cos(teta2);
        float x = (float)Math.cos(teta1) *
                (float)Math.sin(teta2) -
                (float)Math.sin(teta1) * (float)Math.cos(teta2) * (float)Math.cos(delta2);
        float brng = (float)Math.atan2(y, x);
        brng = (float) Math.toDegrees(brng);
        brng = (((int) brng + 360) % 360);
        return brng;
    }

}
