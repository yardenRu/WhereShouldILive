import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Administrator on 22/12/2016.
 */
public class moreFunctions {

    String GETHDIAPARAM = "https://en.wikipedia.org/wiki/List_of_countries_by_Human_Development_Index";
    String GETCOSTOFLIVING =  "https://www.numbeo.com/cost-of-living/rankings_by_country.jsp";
    String GETCRIMERATES = "https://www.numbeo.com/crime/rankings_by_country.jsp";

    public HashMap<String, Double> GetHDIAllCountries(String url) throws IOException {
        HashMap<String, Double> countries = new HashMap<String, Double>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Elements links = doc.getElementsByClass("wikitable");
        int n = 4, i = 0;

        for (Element link : links)
            if (i++ < n) {
                Elements chart = link.getElementsByTag("tr");
                for (Element line : chart) {
                    ArrayList<Element> values = new ArrayList<Element>();
                    for (Element value : line.getElementsByTag("td"))
                        values.add(value);

                    if (values.size() > 0 && values.get(0) != null && values.get(0).hasText() && IsDouble(values.get(0).text())) {
                        String countryName = (values.get(2).text()).replaceAll("\u00A0", "").replaceAll("//s", "");
                        double HDIvalue = Double.parseDouble(values.get(3).text());
                        countries.put(countryName, HDIvalue);
                    }
                }
            } else
                break;
        return countries;
    }


    public HashMap<String, double[]> GetCostOfLivingAllCountries(String url) throws IOException {
        HashMap<String, double[]> countries = new HashMap<String, double[]>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Element link = doc.getElementById("t2");
        Elements chart = link.getElementsByTag("tr");

        for (Element line : chart) {
            ArrayList<Element> values = new ArrayList<Element>();
            for (Element value : line.getElementsByTag("td"))
                values.add(value);

            if (values.size() > 0) {
                String countryName = (values.get(1).text()).replaceAll("\u00A0", "").replaceAll("//s", "");
                double costOfLivingIndex = Double.parseDouble(values.get(2).text());
                double rentIndex = Double.parseDouble(values.get(3).text());
                double localPurchasingPowerIndex = Double.parseDouble(values.get(7).text());
                double[] indexes = {costOfLivingIndex, rentIndex, localPurchasingPowerIndex};
                countries.put(countryName, indexes);
            }
        }
        return countries;
    }


    public static HashMap<String, Double> GetCrimeAllCountries(String url) throws IOException {
        HashMap<String, Double> countries = new HashMap<String, Double>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Element link = doc.getElementById("t2");
        Elements chart = link.getElementsByTag("tr");

        for (Element line : chart) {
            ArrayList<Element> values = new ArrayList<Element>();
            for (Element value : line.getElementsByTag("td"))
                values.add(value);

            if (values.size() > 0) {
                String countryName = (values.get(1).text()).replaceAll("\u00A0", "").replaceAll("//s", "");
                double crimeIndex = Double.parseDouble(values.get(2).text());
                countries.put(countryName, crimeIndex);
            }
        }
        return countries;
    }



    public boolean IsDouble(String num) {
        try
        {
            double number = Double.parseDouble(num);
            //System.out.println(number);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

	public static HashMap<String, HashMap<String, Double>> SetAllToDollar(HashMap<String, HashMap<String, Integer>> dictionaryDB)
{
   HashMap<String, HashMap<String, Double>> newDictionaryDB = new HashMap<String, HashMap<String, Double>>();
   for (String country: dictionaryDB.keySet())
   {
       double conversion = getCountryCurrencyExchange(country);
       HashMap<String, Double> newCountryDB = new HashMap<String, Double>();
       for (String job: dictionaryDB.get(country).keySet())
           newCountryDB.put(job, dictionaryDB.get(country).get(job) * conversion);
       newDictionaryDB.put(country, newCountryDB);
   }
   return newDictionaryDB;
}

// The main function - currently crashes
	public static String BestCountry(int[] ages, String[] proffesions, Double[] income) throws  IOException
{
   ArrayList<String> best5 = new ArrayList<String>();
   String url = "https://en.wikipedia.org/wiki/List_of_countries_by_Human_Development_Index";
   HashMap<String, Double> countriesHDI = GetHDIAllCountries(url);
   url = "https://www.numbeo.com/cost-of-living/rankings_by_country.jsp";
   HashMap<String, double[]> countriesCostOfLiving = GetCostOfLivingAllCountries(url);
   url = "https://www.numbeo.com/crime/rankings_by_country.jsp";
   HashMap<String, Double> countriesCrime = GetCrimeAllCountries(url);
   String database = readFile("C:\\Users\\Administrator\\IdeaProjects\\BestFunc\\src\\MBI_6_EN.txt");
   HashMap<String, HashMap<String, Double>> dictionaryDB = SetAllToDollar(GetSalariesByJobAllCountries(database));


   double maxValue = 0;
   String bestCountry = "";
   ArrayList<Double> values = new ArrayList<Double>();
   for (String country: dictionaryDB.keySet())
   {
       double newYorkSalary = 6878;
       double value  = 0;
       if (!countriesHDI.keySet().contains(country))
           continue;
       double hdi = countriesHDI.get(country);
       if (!countriesCostOfLiving.keySet().contains(country))
           continue;
       double[] indexes = countriesCostOfLiving.get(country);
       double costOfLivingIndex = indexes[0];
       double rentIndex = indexes[1];
       if (!countriesCrime.keySet().contains(country))
           continue;
       double crimes = countriesCrime.get(country);
       double salaries = 0;
       for (String prof: proffesions)
           if (dictionaryDB.get(country).keySet().contains(prof)) {
               if (dictionaryDB.get(country).get(prof) == null)
                   continue;
               salaries += dictionaryDB.get(country).get(prof) / newYorkSalary * 100;
           }
       crimes = Math.pow((100 - crimes) / 100.0, 1.0 / 3);
       rentIndex = Math.pow(rentIndex / 100.0, Math.pow(ages.length, 1 / 2.0));
       double net = salaries - costOfLivingIndex - (rentIndex * 100);
       value = net * hdi * crimes;
       if (maxValue < value)
       {
           maxValue = value;
           bestCountry = country;
       }
   }
   return bestCountry;
}



}