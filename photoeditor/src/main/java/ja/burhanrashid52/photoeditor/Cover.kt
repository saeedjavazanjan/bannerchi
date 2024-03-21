package ja.burhanrashid52.photoeditor

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.get

internal class Cover(
    private val mPhotoEditorView: ViewGroup,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    graphicManager: GraphicManager?
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = graphicManager,
    viewType = ViewType.IMAGE,
    layoutId = R.layout.view_layout_cover
) {
    private var imageView: ImageView? = null
    fun buildView(desiredImage: Bitmap?) {
        imageView?.setImageBitmap(desiredImage)
        imageView!!.adjustViewBounds = true
        imageView!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    private fun setupGesture() {
        val onGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView
        rootView.setOnTouchListener(mMultiTouchListener)
    }


    override fun setupView(rootView: View) {
        imageView = rootView.findViewById(R.id.cover)

    }

   fun getWide():Int{
       return imageView!!.width
   }
    fun getHeight():Int{
       return imageView!!.height
   }



    init {
        setupGesture()
    }
}