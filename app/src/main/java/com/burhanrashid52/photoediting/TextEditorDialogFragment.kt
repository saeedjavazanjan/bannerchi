package com.burhanrashid52.photoediting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.jvm.JvmOverloads
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.burhanrashid52.photoediting.tools.ColorPickerDialog


import com.flask.colorpicker.ColorPickerView

import com.flask.colorpicker.builder.ColorPickerDialogBuilder


/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */
class TextEditorDialogFragment : DialogFragment() {
    public var fontPath: String="fontper/"
    public var fontfamily: String=""
    public var mAddTextEditText: EditText? = null
    var mWonderFont: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val root: View? = null
    private var currentBackgroundColor = R.color.white

    private var mAddTextDoneTextView: TextView? = null
    private var mInputMethodManager: InputMethodManager? = null
    private var mColorCode = 0
    private var colorPickerFragment:ColorPickerDialog?=null
    private var mTextEditorListener: TextEditorListener? = null
    private var englishFonts: RelativeLayout?=null
    private var farsiFonts: RelativeLayout?=null
    private var setColor:RelativeLayout?=null




    interface TextEditorListener {
        fun onDone(inputText: String?, colorCode: Int,typeface: Typeface)
    }

    override fun onStart() {

        if (fontfamily.equals("")){
            fontfamily="Bita.ttf"
        }

        super.onStart()
        val dialog = dialog
        //Make dialog full screen with transparent background
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_text_dialog, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        val addTextColorPickerRecyclerView: RecyclerView =
                view.findViewById(R.id.add_text_color_picker_recycler_view)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        addTextColorPickerRecyclerView.layoutManager = layoutManager
        addTextColorPickerRecyclerView.setHasFixedSize(true)
        val fontPickerAdapter = FontAdapter(activity!!)
        englishFonts=view.findViewById(R.id.relSelectFontE)
        farsiFonts=view.findViewById(R.id.relSelectFontF)
     /*   if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            englishFonts?.visibility=(View.GONE)
            farsiFonts?.visibility=(View.GONE)
            addTextColorPickerRecyclerView.visibility=(View.GONE)
           // mWonderFont = Typeface.createFromAsset(context?.assets, "robot.ttf")
            mWonderFont= context?.let { ResourcesCompat.getFont(it, R.font.iransans) };

            Toast.makeText(context,"در اندروید 8 به پایین امکان تغییر فونت وجود ندارد",Toast.LENGTH_LONG).show()


        }*/


        setColor=view.findViewById(R.id.relSelectColor)

        setColor!!.setOnClickListener {

            ColorPickerDialogBuilder
                    .with(context)

                    .setTitle("Choose color")
                    .initialColor(currentBackgroundColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor -> toast("onColorSelected: 0x" + Integer.toHexString(selectedColor)) }
                    .setPositiveButton("ok") { dialog, selectedColor, allColors ->

                        mAddTextEditText!!.setTextColor(selectedColor)

                        mColorCode=selectedColor
                        }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }
        englishFonts!!.setOnClickListener {
            fontPath="fonteng/"
            fontPickerAdapter.setFarsiFonts(fontPath)
            fontPickerAdapter.setFontpath(fontPath)
          //  fontPickerAdapter.getAssetsFont(fontPath)
            fontPickerAdapter.notifyDataSetChanged()


        }
        farsiFonts!!.setOnClickListener {
            fontPath="fontper/"
            fontPickerAdapter.setFarsiFonts(fontPath)
            fontPickerAdapter.setFontpath(fontPath)
            //  fontPickerAdapter.getAssetsFont(fontPath)
            fontPickerAdapter.notifyDataSetChanged()


        }

        mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
        mAddTextEditText?.isFocusableInTouchMode = true;
        mAddTextEditText?.isFocusable = true;
        mAddTextEditText?.requestFocus();
        if(mWonderFont!=null) {
            mAddTextEditText!!.setTypeface(mWonderFont)
        }
        mInputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv)

        //Setup the color picker for text color
    /*    val toolLayout:LinearLayout=view.findViewById(R.id.fontTools)
        toolLayout.setOnClickListener {

            Toast.makeText(activity!!,"hello",Toast.LENGTH_LONG).show()
        }*/



        fontPickerAdapter.setOnFontSelect(object : FontAdapter.OnFontSelect {
            override fun onFontChanged(fontFamily: String) {
                mWonderFont= Typeface.createFromAsset(mAddTextEditText!!.context.assets, fontPath+fontFamily)
                mAddTextEditText!!.setTypeface(mWonderFont)
                fontfamily=fontFamily

            }
        })
        //This listener will change the text color when clicked on any color from picker
      /*  fontPickerAdapter.setOnColorPickerClickListener(object : OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {
                mColorCode = colorCode
                mAddTextEditText!!.setTextColor(colorCode)
            }
        })*/
        addTextColorPickerRecyclerView.adapter = fontPickerAdapter
        mAddTextEditText!!.setText(arguments!!.getString(EXTRA_INPUT_TEXT))
        mColorCode = arguments!!.getInt(EXTRA_COLOR_CODE)
        mAddTextEditText!!.setTextColor(mColorCode)
        mInputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView!!.setOnClickListener { onClickListenerView ->
            mInputMethodManager!!.hideSoftInputFromWindow(onClickListenerView.windowToken, 0)
            dismiss()
            val inputText = mAddTextEditText!!.text.toString()
            if (!TextUtils.isEmpty(inputText) && mTextEditorListener != null ) {
                if(mWonderFont==null){
                    mWonderFont= Typeface.createFromAsset(mAddTextEditText!!.context.assets, fontPath+"IRANSans.ttf")

                }
                mTextEditorListener!!.onDone(inputText, mColorCode,mWonderFont!!)
            }
        }
    }

    //Callback to listener if user is done with text editing
    fun setOnTextEditorListener(textEditorListener: TextEditorListener) {
        mTextEditorListener = textEditorListener
    }

    companion object {
        private val TAG: String = TextEditorDialogFragment::class.java.simpleName
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"

        //Show dialog with provide text and text color
        //Show dialog with default text input as empty and text color white
        @JvmOverloads
        fun show(
            appCompatActivity: AppCompatActivity,
            inputText: String = "",
            @ColorInt colorCode: Int = ContextCompat.getColor(appCompatActivity, R.color.white)
        ): TextEditorDialogFragment {
            val args = Bundle()
            args.putString(EXTRA_INPUT_TEXT, inputText)
            args.putInt(EXTRA_COLOR_CODE, colorCode)
            val fragment = TextEditorDialogFragment()
            fragment.arguments = args
            fragment.show(appCompatActivity.supportFragmentManager, TAG)
            return fragment
        }
    }

    private fun changeBackgroundColor(selectedColor: Int) {
        currentBackgroundColor = selectedColor
        root!!.setBackgroundColor(selectedColor)
    }

    private fun toast(text: String) {
      //  Toast.makeText(Activity!!, text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

    }
}