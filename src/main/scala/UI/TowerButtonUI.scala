package UI

import scalafx.scene.control.Button
import scalafx.scene.image._
import scalafx.scene.control.Tooltip
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.event.EventIncludes.eventClosureWrapperWithZeroParam
import scala.concurrent.duration.Duration
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class TowerButtonUI(picLoc: String, name: String, price: Int, desc: String) extends Button {
    val image = new Image(picLoc)
    val imageView = new ImageView(image) {
        fitWidth = 80
        fitHeight = 80
        preserveRatio = true
    }
    val ttstyle = ("-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;")
    val tt = new Tooltip()
    tt.setStyle(ttstyle)
    tt.setShowDelay(scalafx.util.Duration.apply(10))
    tt.setGraphic(new VBox {
        spacing = 5
        children = Seq(
            new Label(name) {
                style = ttstyle
            },
            new Label("Price: " + price),
            new Label(desc)
        )
    })
    graphic = imageView
    minWidth = 80
    style = "-fx-background-color: grey;"
    minHeight = 80
    tooltip_=(tt)

    onMouseEntered = () => {
        opacity = 0.5
    }

    onMouseExited = () => {
        opacity = 1
    }
    
}
