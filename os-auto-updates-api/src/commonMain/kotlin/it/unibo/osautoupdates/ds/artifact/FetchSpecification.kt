package it.unibo.osautoupdates.ds.artifact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FetchSpecification")
sealed interface FetchSpecification {
    companion object {
        fun supported() =
            setOf(
                Skip.SERIAL_NAME,
                Local.SERIAL_NAME,
                Remote.SERIAL_NAME,
                Usb.SERIAL_NAME,
            )
    }
}

@Serializable
@SerialName(Skip.SERIAL_NAME)
data object Skip : FetchSpecification {
    const val SERIAL_NAME = "Skip"
}

@Serializable
@SerialName(Local.SERIAL_NAME)
data class Local(
    val roots: List<String>,
    val matcher: String,
) : FetchSpecification {
    companion object {
        const val SERIAL_NAME = "Local"
    }
}

@Serializable
@SerialName(Usb.SERIAL_NAME)
data class Usb(
    val matcher: String,
) : FetchSpecification {
    companion object {
        const val SERIAL_NAME = "Usb"
    }
}

@Serializable
@SerialName(Remote.SERIAL_NAME)
data class Remote(
    val uri: String,
    val fileName: String,
) : FetchSpecification {
    companion object {
        const val SERIAL_NAME = "Remote"
    }
}
