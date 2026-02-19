package com.safebank.bank_api.repository;

import com.safebank.bank_api.domain.BankAccount;

import java.sql.*;
import java.util.Optional;

public class JdbcBankAccountRepository implements BankAccountRepository{

    private static final String DB_URL = "jdbc:h2:./db/safebank";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "secretAdmin";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public Optional<BankAccount> findById(String accountId) {

        String sql = "SELECT id, owner, balance FROM bank_account WHERE id = ?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, accountId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                BankAccount account = new BankAccount(
                        rs.getString("id"),
                        rs.getString("owner"),
                        rs.getBigDecimal("balance")
                );
                return Optional.of(account);
            }
            return  Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void insert(BankAccount account) throws SQLException {

        String sql = "INSERT INTO bank_account (id, owner, balance) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, account.getId());
            ps.setString(2, account.getOwner());
            ps.setBigDecimal(3, account.getBalance());

            ps.executeUpdate();
        }
    }


    private void update(BankAccount account) throws SQLException {

        String sql = "UPDATE bank_account SET owner = ?, balance = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, account.getOwner());
            ps.setBigDecimal(2, account.getBalance());
            ps.setString(3, account.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void save(BankAccount account) {
        try {
            if (existsById(account.getId())) {
                update(account);
            } else {
                insert(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(String accountId) {

        String sql = "SELECT 1 FROM bank_account WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
