package com.techelevator.dao;

import com.techelevator.model.Invite;
import com.techelevator.model.Restaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRestaurantDao implements RestaurantDao {

    private final RestaurantDao restaurantDao;
    private final JdbcTemplate jdbcTemplate;

    public JdbcRestaurantDao(RestaurantDao restaurantDao, JdbcTemplate jdbcTemplate) {
        this.restaurantDao = restaurantDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Restaurant getRestaurantById(int restaurantId) {

        Restaurant restaurant = null;

        String sql = "SELECT r.restaurant_name, r.restaurant_type, r.restaurant_address, r.open_time, r.close_time, " +
                "r.phone_number, r.thumbnail_img, r.star_rating, r.take_out, r.delivery, ir.vetoed " +
                "FROM restaurants r " +
                "JOIN invite_restaurant ir ON r.restaurant_id = ir.restaurant_id " +
                "WHERE restaurant_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, restaurantId);
        if (results.next()) {
            restaurant = mapRowToRestaurant(results);
        }

        return restaurant;
    };

    @Override
    public Restaurant getRestaurantByName(String restaurantName) {

        Restaurant restaurant = null;

        String sql = "SELECT r.restaurant_name, r.restaurant_type, r.restaurant_address, r.open_time, r.close_time, " +
                "r.phone_number, r.thumbnail_img, r.star_rating, r.take_out, r.delivery, ir.vetoed " +
                "FROM restaurants r " +
                "JOIN invite_restaurant ir ON r.restaurant_id = ir.restaurant_id " +
                "WHERE restaurant_name = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, restaurantName);
        if (results.next()) {
            restaurant = mapRowToRestaurant(results);
        }

        return restaurant;
    };

    @Override
    public List<Restaurant> getAllRestaurantsByInviteId(int inviteId) {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT r.restaurant_name, r.restaurant_type, r.restaurant_address, r.open_time, r.close_time, " +
                "r.phone_number, r.thumbnail_img, r.star_rating, r.take_out, r.delivery, ir.vetoed " +
                "FROM restaurants r " +
                "JOIN invite_restaurant ir ON r.restaurant_id = ir.restaurant_id " +
                "JOIN invite i ON ir.invite_id = ir.invite_id " +
                "WHERE invite_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, inviteId);
        while (results.next()){
            Restaurant restaurant = mapRowToRestaurant(results);
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    // Add method for finalists -- SELECT WHERE vetoed = false

    // Some kind of POST method for putting restaurants into DB

    @Override
    public void thumbsDown(int restaurantId) {

        String sql = "UPDATE invite_restaurant " +
                "JOIN restaurants r ON ir.restaurant_id = r.restaurant_id " +
                "SET vetoed = true WHERE restaurant_id = ?";

        //Look into this method type, I think this is still incomplete
    }

    private Restaurant mapRowToRestaurant(SqlRowSet rs) {

        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantId(rs.getInt("restaurant_id"));
        restaurant.setRestaurantName(rs.getString("restaurant_name"));
        restaurant.setRestaurantType(rs.getString("restaurant_type"));
        restaurant.setRestaurantAddress(rs.getString("restaurant_address"));
        restaurant.setOpenTime(rs.getTime("open_time"));
        restaurant.setCloseTime(rs.getTime("close_time"));
        restaurant.setPhoneNumber(rs.getNString("phone_number"));
        restaurant.setThumbnailImage(rs.getString("thumbnail_img"));
        restaurant.setStarRating(rs.getInt("star_rating"));
        restaurant.setTakeOut(rs.getBoolean("take_out"));
        restaurant.setDelivery(rs.getBoolean("delivery"));

        restaurant.setVetoed(rs.getBoolean("vetoed"));

        return restaurant;
    }
}