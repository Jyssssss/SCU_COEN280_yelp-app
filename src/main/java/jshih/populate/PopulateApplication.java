package jshih.populate;

import jshih.model.AppConfiguration;
import jshih.populate.populates.*;

import java.sql.Connection;

public class PopulateApplication {
	public static void main(String[] args) throws Exception {
		if (args.length != 4)
			throw new IllegalArgumentException("Wrong number of arguments.");

		Connection conn = AppConfiguration.getConnection();

		PopulateBusiness.populate(conn, args[0]);
		PopulateUser.populate(conn, args[3]);
		PopulateReview.populate(conn, args[1]);
		PopulateCheckin.populate(conn, args[2]);
	}
}
