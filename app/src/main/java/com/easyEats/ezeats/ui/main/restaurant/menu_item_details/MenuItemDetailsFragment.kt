package com.easyEats.ezeats.ui.main.restaurant.menu_item_details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentMenuItemDetailsBinding
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUICheckBox
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUIRadio
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.main.restaurant.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuItemDetailsFragment : BaseBottomSheetFragment<FragmentMenuItemDetailsBinding>() {

    private val viewModel: RestaurantViewModel by activityViewModels()
    private val args by navArgs<MenuItemDetailsFragmentArgs>()
    val TAG = "MenuItemDetailsFragment"
    override fun setUpViews() {
        binding.menuItem = args.MenuItem
//        binding.ivBack.setOnClickListener {
//            findNavController().navigateUp()
//        }
        Log.d(TAG, "setUpViews: MenuItemDetailsFragment ${args.MenuItem.id}")
    }

    override fun listenToObservers() {


        binding.composeView.setViewCompositionStrategy(
            androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.composeView.setContent {
            val selectedState by viewModel.selected.collectAsState()


            var noteText by remember {
                mutableStateOf("")
            }

            val menuItem = args.MenuItem
            menuItem.subOptions?.let {
                LazyColumn(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Box{
                            Column {
                                AppImage(modifier = Modifier
                                    .fillMaxWidth(),
                                    imageUrl = menuItem.coverPhoto?.url.toString(),
                                    size = 280,
                                    cornerRadius = 0,
                                )

                                HeaderText(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                                    text = menuItem.name ?: "",
                                    fontSize = 24,
                                    fontWeight = FontWeight.Bold,
                                )

                                NormalText(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 3.dp),
                                    text = menuItem.description ?: "",
                                    fontSize = 16,
                                    fontWeight = FontWeight.Normal,
                                    color = AppColor.BlackColor2
                                )
                            }
                        }



                        Spacer(modifier = Modifier.padding(top = 24.dp))
                    }
                    items(it) { item ->
                        if (item?.type == "radio") {
                            MenuItemUIRadio(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                item = item!!,
                                selectedChoiceId = selectedState[item.id!!],
                                onClick = { selectedOption ->
                                    Log.d(TAG, "listenToObservers: ${selectedOption.id}")
                                    viewModel.selectChoice(item.id!!, selectedOption.id!!)
                                }
                            )
                        } else if (item?.type == "checkbox") {
                            MenuItemUICheckBox(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                item = item,
                                selectedChoiceId = selectedState[item.id!!],
                                onClick = { selectedOption ->
                                    viewModel.selectChoice(item.id!!, selectedOption.id!!)
                                }
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_note),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "note"
                            )

                            HeaderText(
                                modifier = Modifier.padding(start = 8.dp),
                                text = stringResource(R.string.any_special_request),
                                fontSize = 14
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            NormalText(
                                text = stringResource(R.string.add_note),
                                fontSize = 12,
                                color = AppColor.BlackColor2
                            )
                        }
                    }
                    item {
                        AppInputField(
                            modifier = Modifier
                                .fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//                                    .padding(start = 8.dp, end = 8.dp)
                            ,
                            text = noteText,
                            onChange = {
                                noteText = it
                            })
                    }
                }
            }
        }

    }


    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuItemDetailsBinding.inflate(layoutInflater)


}