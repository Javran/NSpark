package cmsc724.nspark

import org.apache.spark._
import cmsc724.nspark.Type._

class FacebookGraph(val sCtxt: SparkContext, val paths: FacebookFilePath) {
  val circles: CircleDict = FacebookGraphLoader.loadCircles(sCtxt, paths.circles)
  // edgeRaw contains raw data, ego edges are not yet taken into account
  private val edgeRaw: EdgeDict = FacebookGraphLoader.loadEdges(sCtxt, paths.edges)
  val egoId: NodeId = paths.egoId
  val egoFeat = FacebookGraphLoader.loadEgoFeat(sCtxt, paths.egoFeat)
  val feats: FeatDict = FacebookGraphLoader.loadFeats(sCtxt, paths.feat) + (paths.egoId -> egoFeat)
  val allNodes: Set[NodeId] = feats.keySet
  val edges: EdgeDict =
    allNodes.foldLeft(edgeRaw)((acc, x) => Util.addBiPair((x, egoId), acc))
  val featNames: FeatNameDict = FacebookGraphLoader.loadFeatNames(sCtxt, paths.featNames)
}