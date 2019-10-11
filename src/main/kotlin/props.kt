package kdav.tester


class SimProfiles(val msisdn: String, val iccid: String, val status: String)
class Region(val id: String, val name: String)
class RegionDetails(val region: Region, val status:String, val simProfiles: List<SimProfiles>)
class ServerContext(val regions: List<RegionDetails>)

// Group of Countries
// e.g. asia -> [my, th, in, bd, sg]
typealias CountryGroups = Map<String, List<String>>

// Regions allowed for country
// e.g. sg -> [sg_region, my_region]
typealias RegionsForCountries = Map<String, List<String>>


fun testAsiaGroup() {

    val sgDetails = RegionDetails(
        Region("sg_region", "Singapore Region"),
        "AVAILABLE",
        emptyList())
    val myDetails = RegionDetails(
        Region("my_region", "Malaysia Region"),
        "AVAILABLE",
        emptyList())

    val countryGroups: CountryGroups = mapOf(
        "asia" to listOf("my", "th", "in"),
        "europe" to listOf("no", "se", "dk"))

    val regionsForCountries: RegionsForCountries = mapOf(
        "my" to listOf("my_region", "sg_region", "th_region"),
        "bd" to listOf("my_region"),
        "th" to listOf("th_region"),
        "in" to listOf("my_region"))

    val serverContext = ServerContext(listOf(sgDetails, myDetails))

    // Find country list for the Country group . (For e.g. asia)
    val countryList = countryGroups["asia"]
    if (countryList.isNullOrEmpty()) return

    // Find all allowed regions for the countries in the group (for e.g all countries in asia group)
    val regionListForCountries: List<String> = countryList.flatMap { regionsForCountries[it] ?: emptyList() }

    // Find regions available for this user using server Context
    val regionListFromServerContext = serverContext.regions.map { it.region.id }

    // Find all the region code available for this user.
    val allowedRegionList = regionListFromServerContext.intersect(regionListForCountries).toList()

    val allowedCountryList =  mutableListOf<String>()
    countryList.forEach {
        val regionsForCountry = regionsForCountries[it]
        if (!regionsForCountry.isNullOrEmpty()) {

            // Find if the region list for this country exist in the allowed regions
            if (regionsForCountry.intersect(allowedRegionList).isNotEmpty()) {
                allowedCountryList.add(it)
            }
        }
    }

    print(allowedCountryList)

}

fun main() {
    testAsiaGroup()
}