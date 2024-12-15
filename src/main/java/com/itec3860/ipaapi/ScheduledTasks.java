package com.itec3860.ipaapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Component
public class ScheduledTasks {

    @Autowired
    BeerDAO beerDAO;



    @Autowired
    CountryDAO countryDAO;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    DateTimeFormatter emailFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.now();
    File logFile = new File("log.txt");
    FileWriter writer;

    Scanner dbUpdateFile;
    ObjectMapper mapper = new ObjectMapper();

    // Use shorter cron for testing
    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "*/30 * * * * *")
    public void updateDb() {

        Path beerUpdatePath = Paths.get("./beerUpdate.json");

        if (beerUpdatePath.toFile().exists()) {

            System.out.println("Beer update file found. Commencing update...\n");

            updateDbWithFile(beerUpdatePath.toFile(), mapper, "Beer");


        } else {

            System.out.println("Beer update file not found. Skipping update...\n");

        }

        Path breweryUpdatePath = Paths.get("./breweryUpdate.json");

        if (breweryUpdatePath.toFile().exists()) {

            System.out.println("Brewery update file found. Commencing update...\n");

            updateDbWithFile(beerUpdatePath.toFile(), mapper, "Brewery");


        } else {

            System.out.println("Brewery update file not found. Skipping update...\n");

        }

        Path countryUpdatePath = Paths.get("./countryUpdate.json");

        if (countryUpdatePath.toFile().exists()) {

            System.out.println("Country update file found. Commencing update...\n");

            updateDbWithFile(countryUpdatePath.toFile(), mapper, "Country");


        } else {

            System.out.println("Brewery update file not found. Skipping update...\n");

        }

    }

    // Use shorter cron for testing
    @Scheduled(cron = "0 0 8 */30 * *")
    //@Scheduled(cron = "*/30 * * * * *")
    public void monthlyEmail() throws Exception {

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.trust", "sandbox.smtp.mailtrap.io");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("4e2e59c4403fc7", "e08029496ddb9d");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("host@ipaapi.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("lmotta@ggc.edu"));
        message.setSubject("Enjoy This FREE Suggestion! 🍻");

        LocalDateTime now = LocalDateTime.now();
        Beer beer;
        beer = beerDAO.getRandomBeer();
        String msg =
                "<p style=\"color: blue; text-align: center; font-style: italic;\">"
                + "This email was sent on: "
                + now.format(emailFormatter) + "</p><br>"
                + "<h1 style=\"text-align: center; margin-top: 0px; margin-bottom: 10px;font-family: sans-serif; font-size: 3rem; background: linear-gradient(to right, #ef5350, #f48fb1, #7e57c2, #2196f3, #26c6da, #43a047, #eeff41, #f9a825, #ff5722); -webkit-background-clip: text; -webkit-text-fill-color: transparent;\">Hey! Have you tried this beer? We think you'll love it!</h1>"
                + "<br><hr><p style=\"text-align: center;\">Name: " + beer.getName()
                + "<br>ABV: " + beer.getAbv()
                + "<br>Flavor Notes: " + beer.getFlavorNotes() + "</p>"
                + "<hr><br><br>" + "<h2 style=\"text-align: center;\">Cheers! 🍻</h2>"
                + "<p style=\"text-align: center;\">Sent with ❤️ by the IPA API team!</p>";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("Monthly Email Sent!");

    }

    // Use shorter cron for testing
    @Scheduled(cron = "0 0 23 * */3 *")
    // @Scheduled(cron = "*/30 * * * * *")
    public void updateSeasonal() {

        System.out.println("Commencing seasonal update!");

        // Comment LocalDateTime and use int for testing
        int currentMonth = LocalDateTime.now().getMonthValue();
        // int currentMonth = 10;
        List<Beer> beers = beerDAO.getAllBeers();

        for (Beer beer : beers) {

            // Month 12, 1, and 2
            if ((currentMonth < 3 || currentMonth == 12)
                    && beer.getFlavorNotes().equalsIgnoreCase("Winter")) {

                System.out.println("Seasonal beer found: " + beer + "\n");

                beer.setSeasonal(true);
                beerDAO.update(beer);
                System.out.println("Beer updated: " + beer + "\n");

                // Month 3, 4, and 5
            } else if ((currentMonth >= 3 && currentMonth < 6)
                    && beer.getFlavorNotes().equalsIgnoreCase("Spring")) {

                System.out.println("Seasonal beer found: " + beer + "\n");

                beer.setSeasonal(true);
                beerDAO.update(beer);
                System.out.println("Beer updated: " + beer + "\n");

                // Month 6, 7, and 8
            } else if ((currentMonth >= 6 && currentMonth < 9)
                    && beer.getFlavorNotes().equalsIgnoreCase("Summer")) {

                System.out.println("Seasonal beer found: " + beer + "\n");

                beer.setSeasonal(true);
                beerDAO.update(beer);
                System.out.println("Beer updated: " + beer + "\n");

                // Month 9, 10, and 11
            } else if ((currentMonth >= 9 && currentMonth < 12)
                    && beer.getFlavorNotes().equalsIgnoreCase("Autumn")) {

                System.out.println("Seasonal beer found: " + beer + "\n");

                beer.setSeasonal(true);
                beerDAO.update(beer);
                System.out.println("Beer updated: " + beer + "\n");

            } else {

                System.out.println("Non-seasonal beer found: " + beer + "\n");

                beer.setSeasonal(false);
                beerDAO.update(beer);
                System.out.println("Beer updated: " + beer + "\n");

            }

        }

    }

    // Use shorter cron for testing
    @Scheduled(cron = "0 0 */1 * * *")
    // @Scheduled(cron = "*/15 * * * * *")
    public void checkStatus() {

        LocalDateTime now = LocalDateTime.now();
        int numBeers = beerDAO.getTotalBeers();
        int numBreweries = beerDAO.getTotalBeers();
        int numCountries = beerDAO.getTotalBeers();
        float avgRating = beerDAO.getAverageRating();
        Duration uptime = Duration.between(startTime, now);

        try {
            writer = new FileWriter(logFile, true);

            writer.write("\n\\----------------------------------------/\n");
            writer.write("System time is: " + formatter.format(now) + "\n");
            writer.write("\\----------------------------------------/\n");
            writer.write("Number of beers: " + numBeers + "\n");
            writer.write("------" + "\n");
            writer.write("Number of breweries: " + numBreweries + "\n");
            writer.write("------" + "\n");
            writer.write("Number of countries: " + numCountries + "\n");
            writer.write("------" + "\n");
            writer.write("Total records: " + numBeers + numBreweries + "\n");
            writer.write("------" + "\n");
            writer.write("Average rating: " + avgRating + "\n");
            writer.write("------" + "\n");
            writer.write("Uptime: " + uptime.toDays() + " days " + uptime.toHours() + " hours " + uptime.toMinutes() + " minutes " + uptime.toSeconds() + " seconds\n");
            writer.write("\\----------------------------------------/\n\n");
            writer.close();
            System.out.println("Log file successfully updated!");

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Caught IO Exception while updating log.");

        }

    }

    public boolean updateDbWithFile(File file, ObjectMapper mapper, String type) {

        int updateType;

        if (type.equalsIgnoreCase("beer")) {

            updateType = 1;

        } else if (type.equalsIgnoreCase("brewery")) {

            updateType = 2;

        } else if (type.equalsIgnoreCase("country")) {

            updateType = 3;

        } else {

            System.out.println("updateDbWithFile method called with invalid type.");
            return false;

        }

        switch (updateType) {

            case 1:

                try {

                    List<Beer> beers = mapper.readValue(file, new TypeReference<List<Beer>>() {});

                    for (Beer beer : beers) {

                        int id = beerDAO.checkRecord(beer.getName(), 1);

                        if (id == -1) {

                            beerDAO.update(beer);

                        } else {

                            System.out.println("\n" + beer.getName() + " is already in the database. Updating...");

                            beer.setId(id);

                            beerDAO.update(beer);

                        }

                    }

                    System.out.println("\nBeers updated successfully!\n");

                    return true;

                } catch (Exception e) {

                    System.out.println("\nException caught while creating list of beers from JSON file:");

                    System.out.println(e.getMessage() + "\n");

                    return false;

                }

            case 2:

                // TODO: Brewery branch


            case 3:

                try {

                    List<Country> countries = mapper.readValue(file, new TypeReference<List<Country>>() {});

                    for (Country country : countries) {

                        int id = beerDAO.checkRecord(country.getName(), 3);

                        if (id == -1) {

                            countryDAO.update(country);

                        } else {

                            System.out.println("\n" + country.getName() + " is already in the database. Updating...");

                            country.setId(id);

                            countryDAO.update(country);

                        }

                    }

                    System.out.println("\nCountries updated successfully!\n");

                    return true;

                } catch (Exception e) {

                    System.out.println("\nException caught while creating list of countries from JSON file:");

                    System.out.println(e.getMessage() + "\n");

                    return false;

                }


        }

        return false;

    }

}
