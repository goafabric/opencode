package org.goafabric.containerui.adapter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawImage {
    @JsonProperty("id")            var id: String? = null
    @JsonProperty("configuration") var configuration: RawImageConfig? = null
    @JsonProperty("variants")      var variants: List<RawVariant>? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawImageConfig {
    @JsonProperty("name")         var name: String? = null
    @JsonProperty("creationDate") var creationDate: String? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawVariant {
    @JsonProperty("size") var size: Long? = null
}
