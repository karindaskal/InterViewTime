package com.example.android2projectnew.Module;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.android2projectnew.MainActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import okhttp3.Response;


public class Comparable implements Comparator<Filter> {
    private double x;
    private double y;
    Context context;


    public Comparable(double x, double y, Context context) {
        this.x = x;
        this.y = y;
        this.context = context;
    }

    @Override
    public int compare(Filter o1, Filter o2) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double dis1 = 0;
        double dis2 = 0;

        dis1 = distance(x, y, o1.getLatitude(), o1.getLongitude());
        dis2 = distance(x, y, o2.getLatitude(), o2.getLongitude());

        if (dis1 > dis2)
            return 1;

        else if (dis1 == dis2)
            return 0;

        else return -1;
    }


    @Override
    public Comparator<Filter> reversed() {
        return null;
    }

    @Override
    public Comparator<Filter> thenComparing(Comparator<? super Filter> other) {
        return null;
    }

    @Override
    public <U> Comparator<Filter> thenComparing(Function<? super Filter, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return null;
    }

    @Override
    public <U extends java.lang.Comparable<? super U>> Comparator<Filter> thenComparing(Function<? super Filter, ? extends U> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Filter> thenComparingInt(ToIntFunction<? super Filter> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Filter> thenComparingLong(ToLongFunction<? super Filter> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Filter> thenComparingDouble(ToDoubleFunction<? super Filter> keyExtractor) {
        return null;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = lat2 - lat1;
        double deltaLon = lng2 - lng1;
        double angle = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(deltaLat / 2), 2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                                Math.pow(Math.sin(deltaLon / 2), 2)));
        return radius * angle;

/*        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;
        dist =Math.abs(dist);


        return dist;*/
    }
}


