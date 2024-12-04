data class ORSResponse(
    val features: List<Feature>
)

data class Feature(
    val properties: Properties
)

data class Properties(
    val summary: Summary,
    val segments: List<Segment>
)

data class Summary(
    val distance: Double, // dalam meter
    val duration: Double  // dalam detik
)

data class Segment(
    val distance: Double, // dalam meter
    val duration: Double, // dalam detik
    val steps: List<Step>
)

data class Step(
    val instruction: String,
    val distance: Double, // dalam meter
    val duration: Double  // dalam detik
)

data class RouteRequest(
    val coordinates: List<List<Double>>  // koordinat sebagai list [longitude, latitude]
)
