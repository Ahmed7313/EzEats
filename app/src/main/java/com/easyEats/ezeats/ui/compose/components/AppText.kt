package com.daman.edman.screens.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R

import com.easyEats.ezeats.ui.grayColor

@Composable
fun AppTextHeader(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Medium,
    color: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start

) {

    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily(
                if (fontWeight == FontWeight.Bold)
                    Font(R.font.gilroy_700)
                else if (fontWeight == FontWeight.Medium || fontWeight == FontWeight.Normal)
                    Font(R.font.gilroy_500)
                else
                    Font(R.font.gilroy_400)
            ),
            fontWeight = fontWeight,
            fontSize = fontSize.sp,
            color = color,
            lineHeight = fontSize.sp,
            letterSpacing = 0.5.sp,
            textAlign = textAlign,
        ),
        fontWeight = fontWeight,
        fontFamily = FontFamily(
            if (fontWeight == FontWeight.Bold)
                Font(R.font.gilroy_700)
            else if (fontWeight == FontWeight.Medium || fontWeight == FontWeight.Normal)
                Font(R.font.gilroy_500)
            else
                Font(R.font.gilroy_400)
        ),
        modifier = modifier
    )
}

@Composable
fun AppTextNormal(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 10,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = grayColor,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontFamily = FontFamily(
                if (fontWeight == FontWeight.Bold)
                    Font(R.font.gilroy_700)
                else if (fontWeight == FontWeight.Medium || fontWeight == FontWeight.Normal)
                    Font(R.font.gilroy_500)
                else
                    Font(R.font.gilroy_400)
            ),
            fontWeight = fontWeight,
            fontSize = fontSize.sp,
            color = color,
            lineHeight = fontSize.sp,
            letterSpacing = 0.5.sp
        ),
        fontFamily = FontFamily(
            if (fontWeight == FontWeight.Bold)
                Font(R.font.gilroy_700)
            else if (fontWeight == FontWeight.Medium || fontWeight == FontWeight.Normal)
                Font(R.font.gilroy_500)
            else
                Font(R.font.gilroy_400)
        ),
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}


@Preview
@Composable
fun PreviewheaderText() {
    Column {
        AppTextHeader(text = "اختر اللغة")
        AppTextNormal(text = "hojv gym")
    }

}

@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    BoxWithConstraints(modifier = modifier) {
        var shrunkFontSize = fontSize
        val calculateIntrinsics = @Composable {
            ParagraphIntrinsics(
                text, TextStyle(
                    color = color,
                    fontSize = shrunkFontSize,
                    fontWeight = fontWeight,
                    textAlign = textAlign,
                    lineHeight = lineHeight,
                    fontFamily = fontFamily,
                    textDecoration = textDecoration,
                    fontStyle = fontStyle,
                    letterSpacing = letterSpacing
                ),
                density = LocalDensity.current,
                resourceLoader = LocalFontLoader.current
            )
        }

        var intrinsics = calculateIntrinsics()
        with(LocalDensity.current) {
            while (intrinsics.maxIntrinsicWidth > maxWidth.toPx()) {
                shrunkFontSize *= 0.9
                intrinsics = calculateIntrinsics()
            }
        }
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = shrunkFontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            onTextLayout = onTextLayout,
            style = style
        )
    }
}