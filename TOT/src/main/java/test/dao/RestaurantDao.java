package test.dao;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;

import test.bean.Restaurant;
import test.interfaces.restaurantInterface;

public class RestaurantDao implements restaurantInterface {
	private static Reader reader = null;
	private static SqlSessionFactory ssf = null;

	static {
		try {
			reader = Resources.getResourceAsReader("test/conf/configuration.xml");
			ssf = new SqlSessionFactoryBuilder().build(reader);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public int insertRestaurant(Restaurant restaurant) throws Exception {
		try(SqlSession ss = ssf.openSession()){
			int result = ss.insert("Restaurant.insertRestaurant", restaurant);
			ss.commit();
			return result;			
		}
	}
		
	
	@Override
	public List<Restaurant> selectRestaurant() throws Exception{
			return ssf.openSession().selectList("Restaurant.selectRestaurant");
	}






	
	

}
