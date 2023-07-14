package org.example.DataBase;

import org.example.StatusRuquest;
import org.example.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {
    static final String HOST_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String DB_NAME = "studs2";
    static final String USER = "postgres";
    static final String PASS = "1123";
    public Repository(){
        createDataBase();
        Connection connection = createConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection createConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return null;
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(HOST_URL+DB_NAME, USER, PASS);
            //connection = DriverManager.getConnection(DB_URL);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }

        return connection;
    }
    public void createDataBase(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(HOST_URL,
                    USER, PASS);
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(sql);
            connection.close();
            createTables();
        } catch (SQLException e) {

        }
    }
    public void createTables(){
        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            String query = Files.readString(Path.of("script.txt"));
            statement.executeUpdate(query);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Organization> getOrganisations() {
        Connection connection = createConnection();
        String sql = "SELECT * FROM organization INNER JOIN adress ON adress.id=organization.postalAddressid;";
        List<Organization> organizations = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql); //выполняет запрос
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                long employeesCount = resultSet.getLong("employeesCount");
                int organisationType = resultSet.getInt("orgtype");
                OrganizationType type = OrganizationType.values()[organisationType];
                int postalAddress = resultSet.getInt("postaladdressid");
                String zipcode = resultSet.getString("zipcode");
                Address address = new Address(zipcode);
                address.setId(postalAddress);
                Organization organization = new Organization(fullName, employeesCount, type, address);
                organization.setId(id);
                organizations.add(organization);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return organizations;
    }

    public List<Worker> getWorkers() {
        List<Organization> organizations = getOrganisations();
        Connection connection = createConnection();
        String sql = "SELECT * FROM worker INNER JOIN coordinates ON coordinates.id=worker.coordinatestId;";
        List<Worker> workers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql); //выполняет запрос
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int coordinatestId = resultSet.getInt("coordinatestId");
                float x = resultSet.getFloat("x");
                long y = resultSet.getLong("y");
                Coordinates coordinates = new Coordinates(x, y);
                LocalDate creationDate = resultSet.getDate("creationDate").toLocalDate();
                //OrganizationType type = OrganizationType.values()[organisationType];
                float salary = resultSet.getInt("salary");
                LocalDate localDate = resultSet.getDate("localDate").toLocalDate();
                int position = resultSet.getInt("position");
                Position position1 = Position.values()[position];
                int status = resultSet.getInt("status");
                Status status1 = Status.values()[status];
                int organisationId = resultSet.getInt("organisationId");
                String creator = resultSet.getString("creator");
                Organization organization = organizations.stream().filter(z -> z.getId() == organisationId).findFirst().get();
                Worker worker = new Worker(id, name, coordinates, creationDate, salary, localDate, position1, status1, organization);
                worker.setCreator(creator);
                workers.add(worker);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return workers;
    }


    public void addOrganisation(Organization organization) {
        Connection connection = createConnection();
        String sql = "INSERT INTO adress values(default, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, organization.getPostalAddress().getZipCode());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int generatedKey = 0;
            if (resultSet.next()){
                generatedKey = resultSet.getInt(1);
            }
            organization.getPostalAddress().setId(generatedKey);
            String sql1 = "INSERT INTO organization values(default, ?, ?, ?, ?);";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, organization.getFullName());
            preparedStatement1.setLong(2, organization.getEmployeesCount());
            preparedStatement1.setInt(3, organization.getType().ordinal());
            preparedStatement1.setInt(4, generatedKey);
            preparedStatement1.execute();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            int organizationId = 0;
            if (resultSet1.next()){
                organizationId = resultSet1.getInt(1);
            }
            organization.setId(organizationId);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addWorker(Worker worker) {
        List<Organization> organizations = getOrganisations();
        Optional<Organization> organization = organizations.stream().filter(t -> t.getFullName().equals(worker.getOrganization().getFullName())).findFirst();
        if (organization.isPresent()){
            worker.setOrganization(organization.get());
        } else {
            addOrganisation(worker.getOrganization());
        }
        Connection connection = createConnection();
        String sql = "INSERT INTO coordinates values (default, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, worker.getCoordinates().getX());
            preparedStatement.setLong(2, worker.getCoordinates().getY());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int coordinatesId = 0;
            if (resultSet.next()){
                coordinatesId = resultSet.getInt(1);
            }
            String sql1 = "INSERT INTO worker values(default, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, worker.getName());
            preparedStatement1.setInt(2, coordinatesId);
            preparedStatement1.setDate(3, Date.valueOf(worker.getCreationDate()));
            preparedStatement1.setFloat(4, worker.getSalary());
            preparedStatement1.setDate(5, Date.valueOf(worker.getStartDate()));
            preparedStatement1.setInt(6, worker.getPosition().ordinal());
            preparedStatement1.setInt(7, worker.getStatus().ordinal());
            preparedStatement1.setInt(8, worker.getOrganization().getId());
            preparedStatement1.setString(9, worker.getCreator());
            preparedStatement1.execute();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            int workerId = 0;
            if (resultSet1.next()){
                workerId = resultSet1.getInt(1);
            }
            worker.setId((long) workerId);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User user) {
        Connection connection = createConnection();
        String sql = "INSERT INTO users values (default, ?, ?);";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //preparedStatement1.setInt(1, user.getId());
            preparedStatement1.setString(1, user.getLogin());
            String password = MD2Hash.encryptThisString(user.getPassword());
            preparedStatement1.setString(2, password);
            preparedStatement1.execute();
            ResultSet resultSet = preparedStatement1.getGeneratedKeys();
            int uresId = 0;
            if (resultSet.next()) {
                uresId = resultSet.getInt(1);
            }
            user.setId(uresId);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkLogin(String login){
        Connection connection = createConnection();
        String sql = "SELECT * FROM users WHERE login=?;";
        PreparedStatement preparedStatement1 = null;
        boolean check;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setString(1, login);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            check = resultSet1.next();
            connection.close();
            return !check;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUser(String login, String password) {
        Connection connection = createConnection();
        password = MD2Hash.encryptThisString(password);
        String sql = "SELECT * FROM users WHERE password=? and login=?;";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setString(1, password);
            preparedStatement1.setString(2, login);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            User user = null;
            if (resultSet1.next()) {
                int id = resultSet1.getInt("id");
                user = new User(id, login, password);
            }
            connection.close();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteWorker(long id) {
        Connection connection = createConnection();
        String sql = "DELETE FROM worker WHERE id=?;";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setLong(1, id);
            preparedStatement1.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeAllBySalary(float salary){
        Connection connection = createConnection();
        String sql = "DELETE FROM worker WHERE salary=?;";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setFloat(1, salary);
            preparedStatement1.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeGreater(float salary){
        Connection connection = createConnection();
        String sql = "DELETE FROM worker WHERE salary>?;";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setFloat(1, salary);
            preparedStatement1.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeAll(){
        Connection connection = createConnection();
        String sql = "DELETE FROM worker;";
        Statement statemant = null;
        try {
            statemant = connection.createStatement();
            statemant.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCoordinatesId(Coordinates coordinates){
        Connection connection = createConnection();
        String sql = "UPDATE Coordinates SET x=?, y=? WHERE id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setFloat(1,coordinates.getX());
            preparedStatement.setLong(2,coordinates.getY());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWorker(Worker worker){
        updateCoordinatesId(worker.getCoordinates());
        Connection connection = createConnection();
        String sql = "UPDATE worker SET name=?, salary=?,localDate=?,position=?, status=?, organisationId=?  WHERE id=?";
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setString(1, worker.getName());
            preparedStatement1.setFloat(2, worker.getSalary());
            preparedStatement1.setDate(3, Date.valueOf(worker.getStartDate()));
            preparedStatement1.setInt(4, worker.getPosition().ordinal());
            preparedStatement1.setInt(5,worker.getStatus().ordinal());
            preparedStatement1.setInt(6, worker.getOrganization().getId());
            preparedStatement1.setLong(7, worker.getId());
            preparedStatement1.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
