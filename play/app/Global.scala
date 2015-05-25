import com.kenshoo.play.metrics.MetricsFilter
import play.api.mvc.WithFilters

object Global extends WithFilters(MetricsFilter) {

}