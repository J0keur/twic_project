
package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dto.Coordonnees;
import com.dto.Ville;
import java.sql.PreparedStatement;

@Repository
public class VilleDAO {

	@Autowired
	private DaoFactory daoFactory;

	public VilleDAO(DaoFactory daoFactory) {
		this.daoFactory = new DaoFactory();
	}

	public List<Ville> Villes() throws SQLException {
		List<Ville> villes = new ArrayList<Ville>();
		Connection connexion = null;
		ResultSet resultat = null;
		Statement statement = null;

		try {

			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultat = statement.executeQuery("SELECT " + "* " + "FROM " + "ville_france;");
			while (resultat.next()) {
				villes.add(new Ville(resultat.getString(2), resultat.getString(3),
						new Coordonnees(resultat.getString(7), resultat.getString(6)), resultat.getString(1),
						resultat.getString(4), resultat.getString(5)));
			}
			statement.close();
			resultat.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return villes;

	}

	public List<Ville> getVillesByInsee(String insee) {
		List<Ville> villes = new ArrayList<Ville>();
		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;

		try {
			connexion = daoFactory.getConnection();
			statement = connexion.prepareStatement(("SELECT * FROM ville_france WHERE Code_commune_INSEE= ?"));
			statement.setString(1, insee);
			resultat = statement.executeQuery();

			while (resultat.next()) {
				villes.add(new Ville(resultat.getString(2), resultat.getString(3),
						new Coordonnees(resultat.getString(7), resultat.getString(6)), resultat.getString(1),
						resultat.getString(4), resultat.getString(5)));

			}
			statement.close();
			resultat.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return villes;
	}

	public Ville ajouterVille(Ville ville) throws SQLException {
		Connection connexion = null;
		PreparedStatement prepared = null;

		try {
			connexion = daoFactory.getConnection();
			prepared = connexion.prepareStatement(
					"INSERT INTO ville_france (Code_commune_INSEE,Nom_commune,Code_postal,Libelle_acheminement,Ligne_5,Longitude,Latitude)"
							+ "VALUES ( ? , ? , ? , ? , ? , ? , ? )");
			prepared.setString(1, ville.getCodeInsee());
			prepared.setString(2, ville.getNom());
			prepared.setString(3, ville.getCodePostal());
			prepared.setString(4, ville.getLibelle());
			prepared.setString(5, ville.getLigne5());
			prepared.setString(6, ville.getCoordonnees().getLongitude());
			prepared.setString(7, ville.getCoordonnees().getLatitude());
			prepared.executeUpdate();
			prepared.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return ville;

	}

	public void modifierVille(Ville ville, String insee) {
		Connection connexion = null;
		PreparedStatement prepared = null;

		try {
			connexion = daoFactory.getConnection();
			prepared = connexion.prepareStatement(
					"UPDATE ville_france SET Code_commune_INSEE=?, Nom_commune =?, Code_postal=?, Libelle_acheminement=?, Ligne_5=?, Longitude=?, Latitude=? WHERE Code_commune_INSEE=?;");
			prepared.setString(1, ville.getCodeInsee());
			prepared.setString(2, ville.getNom());
			prepared.setString(3, ville.getCodePostal());
			prepared.setString(4, ville.getLibelle());
			prepared.setString(5, ville.getLigne5());
			prepared.setString(6, ville.getCoordonnees().getLongitude());
			prepared.setString(7, ville.getCoordonnees().getLatitude());
			prepared.setString(8, insee);
			prepared.executeUpdate();
			prepared.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void supprimerVille(String insee) {
		Connection connexion = null;
		PreparedStatement prepared = null;

		try {
			connexion = daoFactory.getConnection();
			prepared = connexion.prepareStatement("DELETE FROM ville_france WHERE Code_commune_INSEE=?;");
			prepared.setString(1, insee);
			prepared.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
