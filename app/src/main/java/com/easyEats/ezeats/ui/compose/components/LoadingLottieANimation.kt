package com.thub.uadadvertisor.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.easyEats.ezeats.R


@Composable
fun LoadingAnimation(modifier: Modifier = Modifier,rawRes: Int = R.raw.loading_icon) {
    var isPlaying by remember {
        mutableStateOf(true)
    }

// for speed
    var speed by remember {
        mutableStateOf(1f)
    }



// remember lottie composition, which
// accepts the lottie composition result
    val composition by rememberLottieComposition(

        LottieCompositionSpec
            // here `code` is the file name of lottie file
            // use it accordingly
            .RawRes(rawRes)
    )

    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart
        // when paused and play
        // pass false to continue the animation
        // at which is was paused
        restartOnPlay = false

    )

    LottieAnimation(composition = composition, progress = progress, modifier = modifier)
}