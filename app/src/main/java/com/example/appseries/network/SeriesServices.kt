package com.example.appseries.network

import com.example.appseries.model.Serie

class SeriesServices {

    private val listSeries = listOf<Serie>(
        Serie(
            "Shingeki no kyojin",
            "La historia nos traslada a un mundo en el que la humanidad estuvo a punto de ser exterminada " +
                    "cientos de años atrás por los gigantes. Los gigantes son enormes, parecen no ser inteligentes" +
                    " y devoran seres humanos. Lo peor es que parece que lo hacen por placer y no por alimentarse. " +
                    "Una pequeña parte de la humanidad ha conseguido sobrevivir protegiéndose en una ciudad con unos " +
                    "altísimos muros, más altos que el mayor de los gigantes. La ciudad vive su vida tranquila, y hace" +
                    " más de 100 años que ningún gigante aparece por allí. Eren y su hermana adoptiva Mikasa son " +
                    "todavía unos adolescentes cuando ven algo horroroso: un gigante mucho mayor que todos los que " +
                    "la humanidad había conocido hasta el momento está destruyendo los muros de la ciudad. No pasa mucho" +
                    " tiempo hasta que los gigantes entran por el hueco abierto en el muro y comienzan a devorar a la gente.",
            "https://www.latercera.com/resizer/X8GDI9YjD0qVHEMmPrGaEnGEYz0=/900x600/smart/arc-anglerfish-arc2-prod-copesa.s3.amazonaws.com/public/CXE25YCTW5BWZFU2RV5AFJVZKE.jpg",
            true

        ),

        Serie(
            "Karakai Jouzu no Takagi-san",
            "\"¡Juro que hoy sí lograré avergonzar a Takagi-san!\". Nishikata es un estudiante de secundaria a " +
                    "quien su compañera y vecina en clase, Takagi-san, no deja de molestar y avergonzar en todo momento." +
                    "Él intenta vengarse día tras día de ella, pero Takagi siempre parece ir un paso por delante. ¿Logrará " +
                    "hacer que sea ella la que se avergüence algún día? ¡La batalla de voluntades de juventud entre " +
                    "Nishikata y Takagi da comienzo!",
            "https://universo-nintendo.com.mx/my_uploads/2019/06/Poster-Karakai-Jouzu-no-Takagi-san-2.jpg",
            true

        ),

        Serie(
            "Made in Abyss",
            "En esta obra, un enorme sistema de cuevas llamado el “Abismo” es el único lugar inexplorado del mundo. " +
                    "Extrañas y poderosas criaturas residen en sus profundidades, junto a preciadas reliquias que los humanos " +
                    "son incapaces de producir. Los misterios del Abismo fascinan a los humanos y estos bajan a sus profundidades. " +
                    "Los aventureros que se adentran en él son conocidos como “Cave Raiders”. Una joven huérfana llamada Rico" +
                    " vive en el pueblo Osu en el filo del Abismo. Su sueño es convertirse en una “Cave Raider”, como su madre," +
                    "y solventar los misterios del sistema de cuevas. Un día, Rico comienza a explorar las cuevas y descubre un " +
                    "robot con aspecto de un chico humano.",
            "https://animeflv.net/uploads/animes/covers/2767.jpg"
        ),

        Serie(
            "Uzaki-chan wa Asobitai!",
            "Shinichi Sakurai solo desea un poco de paz y silencio en su vida. pero Hana Uzaki (su ruidosa y bien desarrollada " +
                    "kouhai) tiene otros planes. Todo lo que ella quiere es pasar el rato con él y molestarlo un poco. ¡Con la ayuda " +
                    "de su encanto e insistencia, este podría ser el inicio de una hermosa relación!",
            "https://animeflv.net/uploads/animes/covers/3327.jpg"
        )
    )

    fun getSeriesService(): List<Serie> {
        return listSeries
    }

    fun getSeriesFavService(): List<Serie> {
        return listSeries.filter { it.fav }
    }
}