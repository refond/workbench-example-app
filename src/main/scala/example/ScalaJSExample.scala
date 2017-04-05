package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

case class Point(x: Int, y: Int) {
  def +(p: Point) = Point(x + p.x, y + p.y)
  def /(d: Int) = Point(x / d, y / d)
}

@JSExport
object ScalaJSExample {

  /**
   * Get browser viewport dimensions
   */
  def getBrowserViewportDimensions(): Point = {
    val viewPortWidth = Math.max(dom.document.documentElement.clientWidth, dom.window.innerWidth);
    val viewPortHeight = Math.max(dom.document.documentElement.clientHeight, dom.window.innerHeight);
    Point(x = viewPortWidth.toInt, y = viewPortHeight.toInt)
  }

  @JSExport
  def main(canvas: html.Canvas): Unit = {

    val browserViewportDim = getBrowserViewportDimensions()
    println(s"width: ${browserViewportDim.x}, height: ${browserViewportDim.y}")

    val ctx = canvas.getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.canvas.width = browserViewportDim.x
    ctx.canvas.height = browserViewportDim.y

    val SQUARE_SIZE = Math.min(ctx.canvas.width, ctx.canvas.height)

    var count = 0
    var p = Point(0, 0)
    val corners = Seq(Point(SQUARE_SIZE, SQUARE_SIZE), Point(0, SQUARE_SIZE), Point(SQUARE_SIZE / 2, 0))

    def clear() = {
      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, SQUARE_SIZE, SQUARE_SIZE)
    }

    def run(maxLoops: Int) = for (i <- 0 until 10) {
      if (count % maxLoops == 0) clear()
      count += 1
      p = (p + corners(Random.nextInt(3))) / 2

      val height = 2 * (SQUARE_SIZE + 1) / (SQUARE_SIZE + p.y)
      val r = (p.x * height).toInt
      val g = ((SQUARE_SIZE - p.x) * height).toInt
      val b = p.y
      ctx.fillStyle = s"rgb($g, $r, $b)"

      ctx.fillRect(p.x, p.y, 4, 4)
    }

    val MAX_LOOPS = 9000
    dom.window.setInterval(() => run(MAX_LOOPS), 50)
  }
}
