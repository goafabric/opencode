package org.goafabric.containerui.adapter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawVolume {
    @JsonProperty("id")            var id: String? = null
    @JsonProperty("configuration") var configuration: RawVolumeConfig? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawVolumeConfig {
    @JsonProperty("name")         var name: String? = null
    @JsonProperty("creationDate") var creationDate: String? = null
    @JsonProperty("sizeInBytes")  var sizeInBytes: Long? = null
}
