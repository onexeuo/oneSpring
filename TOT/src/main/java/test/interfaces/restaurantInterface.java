package test.interfaces;

import java.util.List;

import test.bean.Restaurant;

public interface restaurantInterface {
		
		public abstract List<Restaurant> selectRestaurant() throws Exception;
		
		public abstract int insertRestaurant(Restaurant restaurant) throws Exception;
}
