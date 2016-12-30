import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import org.jsoup.select.Elements
import java.io.File
import java.io.IOException
import java.util.*
import java.util.HashMap



/**
 * Created by Administrator on 22/12/2016.
 */
class main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {

            // val d = HashMap<String, Int>()
//            val country = readLine()
//            val curr = getCountryCurrencyInShort(country!!)
//            println("CURRENCY = $curr" )
//            println("EXCHANGE RATE = ${getExchangeRate(curr)}")
            var country: String
            var command: String
            while (true) {
                println("Enter country:")
                country = readLine()!!
                println("Enter command:")
                command = readLine()!!

                when (command.toLowerCase()) {
                    "coin" ->
                            println(getCountryCurrencyInShort(country))
                    "exchange rate" ->
                            println("${getCountryCurrencyInShort(country)} / USD = ${getExchangeRate(getCountryCurrencyInShort(country))}")
                    "hdi" -> // Human development index
                            println("The HDI of $country is ${GetHDIAllCountries()[country]}")
                    "cost of living" ->
                            println("The cost of living in $country = ${GetCostOfLivingAllCountries()[country]}")
                    "crime" ->
                            println("The crime rank of $country relative to NY is ${GetCrimeAllCountries()[country]}")
                    "salary" -> {
                        println("Enter profession: " + arrayOf("Managers", "Professionals", "Technicians and associate professionals",
                                "Clerical support workers", "Service and sales workers", "Skilled agricultural, forestry and fishery workers",
                                "Craft and related trades workers", "Plant and machine operators, and assemblers", "Elementary occupations",
                                "Armed forces occupations").joinToString())
                        val profession = readLine()
                        println("The average salary for the profession $profession is ${GetSalariesByJobAllCountries("C:\\Users\\Administrator\\IdeaProjects\\country_currency_to_dollar\\MBI_6_EN.txt")}")
                    }

                }
            }
        }




        fun convert_currencies(country_money: HashMap<String, Int>)/* : HashMap<String, Double> */ {
            var doc: Document

            country_money.forEach { s, i ->  }
        }

        fun getExchangeRate(currency: String): Double {
            val doc = Jsoup.connect("http://www.xe.com/currencyconverter/convert/?Amount=1&From=$currency&To=USD").get()

            val rateElement = doc.getElementsByClass("uccResultAmount")
//            println("RATE ELEMENT.SIZE = ${rateElement.size}")

            return rateElement[0].text().toDouble()
        }


        @Throws(IOException::class)
        fun GetSalariesByJobAllCountries(database: String): HashMap<String, HashMap<String, Int>> {
            val jobs = arrayOf("Managers", "Professionals", "Technicians and associate professionals", "Clerical support workers", "Service and sales workers", "Skilled agricultural, forestry and fishery workers", "Craft and related trades workers", "Plant and machine operators, and assemblers", "Elementary occupations", "Armed forces occupations")
            val dictionaryDB = HashMap<String, HashMap<String, Int>>()
            for (line in database.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                var i = 0
                var country = ""
                val currentCountryDict = HashMap<String, Int>()
                val salaries = arrayOfNulls<Int>(10)
                for (value in line.split("\u0009".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    if (i == 0)
                        country = value
                    if (i > 13)
                        break
                    if (i > 3) {
                        if (IsDouble(value))
                            salaries[i - 4] = Integer.parseInt(value)
                    }
                    i++
                }
                for (j in 0..9) {
                    currentCountryDict.put(jobs[j], salaries[j]!!)
                }
                dictionaryDB.put(country, currentCountryDict)
            }
            return dictionaryDB
        }


        fun getCountryCurrencyInShort(countryName: String): String {
            //val country = countryName[0].toUpperCase() + countryName.substring(1).toLowerCase()
            val country = countryName
            val doc = Jsoup.connect("https://www.countries-ofthe-world.com/world-currencies.html").get()
            val table = doc.getElementsByClass("codes")[0]
            val rows = table.getElementsByTag("tr")
            var items: Elements
            rows.forEach { row ->
                items = row.getElementsByTag("td")
                if (items.size == 3) {
                    if (country in items[0].text())
                        return items[2].text()
                }
            }

            return "NONE"
        }

        @Throws(IOException::class)
        fun GetHDIAllCountries(url: String="https://en.wikipedia.org/wiki/List_of_countries_by_Human_Development_Index"): HashMap<String, Double> {
            val countries = HashMap<String, Double>()
            val doc: Document
            doc = Jsoup.connect(url).get()
            val links = doc.getElementsByClass("wikitable")
            val n = 4
            var i = 0

            for (link in links)
                if (i++ < n) {
                    val chart = link.getElementsByTag("tr")
                    for (line in chart) {
                        val values = ArrayList<Element>()
                        for (value in line.getElementsByTag("td"))
                            values.add(value)

                        if (values.size > 0 && values[0] != null && values[0].hasText() && IsDouble(values[0].text())) {
                            val countryName = values[2].text().replace("\u00A0".toRegex(), "").replace("//s".toRegex(), "")
                            val HDIvalue = java.lang.Double.parseDouble(values[3].text())
                            countries.put(countryName, HDIvalue)
                        }
                    }
                } else
                    break
            return countries
        }

        @Throws(IOException::class)
        fun GetCostOfLivingAllCountries(url: String = "https://www.numbeo.com/cost-of-living/rankings_by_country.jsp"): HashMap<String, DoubleArray> {
            val countries = HashMap<String, DoubleArray>()
            val doc: Document
            doc = Jsoup.connect(url).get()
            val link = doc.getElementById("t2")
            val chart = link.getElementsByTag("tr")

            for (line in chart) {
                val values = ArrayList<Element>()
                for (value in line.getElementsByTag("td"))
                    values.add(value)

                if (values.size > 0) {
                    val countryName = values[1].text().replace("\u00A0".toRegex(), "").replace("//s".toRegex(), "")
                    val costOfLivingIndex = java.lang.Double.parseDouble(values[2].text())
                    val rentIndex = java.lang.Double.parseDouble(values[3].text())
                    val localPurchasingPowerIndex = java.lang.Double.parseDouble(values[7].text())
                    val indexes = doubleArrayOf(costOfLivingIndex, rentIndex, localPurchasingPowerIndex)
                    countries.put(countryName, indexes)
                }
            }
            return countries
        }

        @Throws(IOException::class)
        fun GetCrimeAllCountries(url: String = "https://www.numbeo.com/crime/rankings_by_country.jsp"): HashMap<String, Double> {
            val countries = HashMap<String, Double>()
            val doc: Document
            doc = Jsoup.connect(url).get()
            val link = doc.getElementById("t2")
            val chart = link.getElementsByTag("tr")

            for (line in chart) {
                val values = ArrayList<Element>()
                for (value in line.getElementsByTag("td"))
                    values.add(value)

                if (values.size > 0) {
                    val countryName = values[1].text().replace("\u00A0".toRegex(), "").replace("//s".toRegex(), "")
                    val crimeIndex = java.lang.Double.parseDouble(values[2].text())
                    countries.put(countryName, crimeIndex)
                }
            }
            return countries
        }

        fun IsDouble(num: String): Boolean {
            try {
                val number = java.lang.Double.parseDouble(num)
                //System.out.println(number);
                return true
            } catch (e: NumberFormatException) {
                return false
            }

        }

        @Throws(IOException::class)
        fun readFile(pathname: String): String {

            val file = File(pathname)
            val fileContents = StringBuilder(file.length().toInt())
            val scanner = Scanner(file)
            val lineSeparator = System.getProperty("line.separator")

            try {
                while (scanner.hasNextLine()) {
                    fileContents.append(scanner.nextLine() + lineSeparator)
                }
                return fileContents.toString()
            } finally {
                scanner.close()
            }
        }

    }
}