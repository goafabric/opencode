package org.goafabric.containerui.adapter.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawContainer {
    @JsonProperty("id")        var id: String? = null
    @JsonProperty("configuration") var configuration: RawContainerConfig? = null
    @JsonProperty("status")    var status: RawContainerStatus? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawContainerConfig {
    @JsonProperty("image")          var image: RawContainerImage? = null
    @JsonProperty("publishedPorts") var publishedPorts: List<RawPort>? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawContainerImage {
    @JsonProperty("reference") var reference: String? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawPort {
    @JsonProperty("hostAddress")    var hostAddress: String? = null
    @JsonProperty("hostPort")       var hostPort: Int? = null
    @JsonProperty("containerPort")  var containerPort: Int? = null
    @JsonProperty("proto")          var proto: String? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawContainerStatus {
    @JsonProperty("state") var state: String? = null
}

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
class RawStats {
    @JsonProperty("id")               var id: String? = null
    @JsonProperty("cpuUsageUsec")     var cpuUsageUsec: Long? = null
    @JsonProperty("memoryUsageBytes") var memoryUsageBytes: Long? = null
    @JsonProperty("memoryLimitBytes") var memoryLimitBytes: Long? = null
}
