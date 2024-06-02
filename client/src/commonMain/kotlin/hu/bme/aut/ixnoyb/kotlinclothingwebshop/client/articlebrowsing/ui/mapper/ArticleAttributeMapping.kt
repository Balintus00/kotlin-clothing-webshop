package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.mapper

import androidx.compose.runtime.Composable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Color
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Index
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Shade
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_beige
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_black
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_blue
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_bluish_green
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_brown
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_green
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_grey
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_khaki_green
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_lilac_purple
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_metal
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_mole
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_orange
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_other
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_pink
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_red
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_turquoise
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_white
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_yellow
import kotlinclothingwebshop.client.generated.resources.article_color_attribute_yellowish_green
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_accessories
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_blouses
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_dressed
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_dresses_ladies
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_dresses_skirts_girls
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_jersey_basic
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_jersey_fancy
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_knitwear
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_other
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_outdoor
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_shirts
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_shoes
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_shorts
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_skirts
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_socks_and_tights
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_special_offers
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_swimwear
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_trousers
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_trousers_denim
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_under_nightwear
import kotlinclothingwebshop.client.generated.resources.article_garment_group_attribute_woven_jersey_knitted_mix_baby
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_all_over_pattern
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_application_3d
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_argyle
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_chambray
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_check
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_colour_blocking
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_contrast
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_dot
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_embroidery
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_front_print
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_glittering_metallic
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_hologram
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_jacquard
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_lace
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_melange
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_mesh
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_metallic
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_mixed_solid_pattern
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_neps
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_other_pattern
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_other_structure
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_placement_print
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_sequin
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_slub
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_solid
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_stripe
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_transparent
import kotlinclothingwebshop.client.generated.resources.article_graphical_appearance_attribute_treatment
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_baby_sizes_50_98
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_children_accessories
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_children_sizes_134_170
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_children_sizes_92_140
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_divided
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_ladies_accessories
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_ladieswear
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_lingeries_tights
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_menswear
import kotlinclothingwebshop.client.generated.resources.article_index_attribute_sport
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_bright
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_dark
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_dusty_light
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_light
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_medium
import kotlinclothingwebshop.client.generated.resources.article_shade_attribute_medium_dusty
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun Color.toDisplayValue(): String = when (this) {
    Color.Beige -> stringResource(Res.string.article_color_attribute_beige)
    Color.Black -> stringResource(Res.string.article_color_attribute_black)
    Color.Blue -> stringResource(Res.string.article_color_attribute_blue)
    Color.BluishGreen -> stringResource(Res.string.article_color_attribute_bluish_green)
    Color.Brown -> stringResource(Res.string.article_color_attribute_brown)
    Color.Green -> stringResource(Res.string.article_color_attribute_green)
    Color.Grey -> stringResource(Res.string.article_color_attribute_grey)
    Color.KhakiGreen -> stringResource(Res.string.article_color_attribute_khaki_green)
    Color.LilacPurple -> stringResource(Res.string.article_color_attribute_lilac_purple)
    Color.Metal -> stringResource(Res.string.article_color_attribute_metal)
    Color.Mole -> stringResource(Res.string.article_color_attribute_mole)
    Color.Orange -> stringResource(Res.string.article_color_attribute_orange)
    Color.Pink -> stringResource(Res.string.article_color_attribute_pink)
    Color.Red -> stringResource(Res.string.article_color_attribute_red)
    Color.Turquoise -> stringResource(Res.string.article_color_attribute_turquoise)
    Color.White -> stringResource(Res.string.article_color_attribute_white)
    Color.Yellow -> stringResource(Res.string.article_color_attribute_yellow)
    Color.YellowishGreen -> stringResource(Res.string.article_color_attribute_yellowish_green)
    Color.Other -> stringResource(Res.string.article_color_attribute_other)
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun GarmentGroup.toDisplayValue(): String = when (this) {
    GarmentGroup.Accessories -> stringResource(Res.string.article_garment_group_attribute_accessories)
    GarmentGroup.Blouses -> stringResource(Res.string.article_garment_group_attribute_blouses)
    GarmentGroup.Dressed -> stringResource(Res.string.article_garment_group_attribute_dressed)
    GarmentGroup.DressesLadies -> stringResource(Res.string.article_garment_group_attribute_dresses_ladies)
    GarmentGroup.DressesSkirtsGirls -> stringResource(Res.string.article_garment_group_attribute_dresses_skirts_girls)
    GarmentGroup.JerseyBasic -> stringResource(Res.string.article_garment_group_attribute_jersey_basic)
    GarmentGroup.JerseyFancy -> stringResource(Res.string.article_garment_group_attribute_jersey_fancy)
    GarmentGroup.Knitwear -> stringResource(Res.string.article_garment_group_attribute_knitwear)
    GarmentGroup.Outdoor -> stringResource(Res.string.article_garment_group_attribute_outdoor)
    GarmentGroup.Shirts -> stringResource(Res.string.article_garment_group_attribute_shirts)
    GarmentGroup.Shoes -> stringResource(Res.string.article_garment_group_attribute_shoes)
    GarmentGroup.Shorts -> stringResource(Res.string.article_garment_group_attribute_shorts)
    GarmentGroup.Skirts -> stringResource(Res.string.article_garment_group_attribute_skirts)
    GarmentGroup.SocksAndTights -> stringResource(Res.string.article_garment_group_attribute_socks_and_tights)
    GarmentGroup.SpecialOffers -> stringResource(Res.string.article_garment_group_attribute_special_offers)
    GarmentGroup.Swimwear -> stringResource(Res.string.article_garment_group_attribute_swimwear)
    GarmentGroup.Trousers -> stringResource(Res.string.article_garment_group_attribute_trousers)
    GarmentGroup.TrousersDenim -> stringResource(Res.string.article_garment_group_attribute_trousers_denim)
    GarmentGroup.UnderNightwear -> stringResource(Res.string.article_garment_group_attribute_under_nightwear)
    GarmentGroup.WovenJerseyKnittedMixBaby -> stringResource(Res.string.article_garment_group_attribute_woven_jersey_knitted_mix_baby)
    GarmentGroup.Other -> stringResource(Res.string.article_garment_group_attribute_other)
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun GraphicalAppearance.toDisplayValue(): String = when (this) {
    GraphicalAppearance.AllOverPattern -> stringResource(Res.string.article_graphical_appearance_attribute_all_over_pattern)
    GraphicalAppearance.Application3D -> stringResource(Res.string.article_graphical_appearance_attribute_application_3d)
    GraphicalAppearance.Argyle -> stringResource(Res.string.article_graphical_appearance_attribute_argyle)
    GraphicalAppearance.Chambray -> stringResource(Res.string.article_graphical_appearance_attribute_chambray)
    GraphicalAppearance.Check -> stringResource(Res.string.article_graphical_appearance_attribute_check)
    GraphicalAppearance.ColourBlocking -> stringResource(Res.string.article_graphical_appearance_attribute_colour_blocking)
    GraphicalAppearance.Contrast -> stringResource(Res.string.article_graphical_appearance_attribute_contrast)
    GraphicalAppearance.Denim -> stringResource(Res.string.article_garment_group_attribute_trousers_denim)
    GraphicalAppearance.Dot -> stringResource(Res.string.article_graphical_appearance_attribute_dot)
    GraphicalAppearance.Embroidery -> stringResource(Res.string.article_graphical_appearance_attribute_embroidery)
    GraphicalAppearance.FrontPrint -> stringResource(Res.string.article_graphical_appearance_attribute_front_print)
    GraphicalAppearance.GlitteringMetallic -> stringResource(Res.string.article_graphical_appearance_attribute_glittering_metallic)
    GraphicalAppearance.Hologram -> stringResource(Res.string.article_graphical_appearance_attribute_hologram)
    GraphicalAppearance.Jacquard -> stringResource(Res.string.article_graphical_appearance_attribute_jacquard)
    GraphicalAppearance.Lace -> stringResource(Res.string.article_graphical_appearance_attribute_lace)
    GraphicalAppearance.Melange -> stringResource(Res.string.article_graphical_appearance_attribute_melange)
    GraphicalAppearance.Mesh -> stringResource(Res.string.article_graphical_appearance_attribute_mesh)
    GraphicalAppearance.Metallic -> stringResource(Res.string.article_graphical_appearance_attribute_metallic)
    GraphicalAppearance.MixedSolidPattern -> stringResource(Res.string.article_graphical_appearance_attribute_mixed_solid_pattern)
    GraphicalAppearance.Neps -> stringResource(Res.string.article_graphical_appearance_attribute_neps)
    GraphicalAppearance.OtherPattern -> stringResource(Res.string.article_graphical_appearance_attribute_other_pattern)
    GraphicalAppearance.OtherStructure -> stringResource(Res.string.article_graphical_appearance_attribute_other_structure)
    GraphicalAppearance.PlacementPrint -> stringResource(Res.string.article_graphical_appearance_attribute_placement_print)
    GraphicalAppearance.Sequin -> stringResource(Res.string.article_graphical_appearance_attribute_sequin)
    GraphicalAppearance.Slub -> stringResource(Res.string.article_graphical_appearance_attribute_slub)
    GraphicalAppearance.Solid -> stringResource(Res.string.article_graphical_appearance_attribute_solid)
    GraphicalAppearance.Stripe -> stringResource(Res.string.article_graphical_appearance_attribute_stripe)
    GraphicalAppearance.Transparent -> stringResource(Res.string.article_graphical_appearance_attribute_transparent)
    GraphicalAppearance.Treatment -> stringResource(Res.string.article_graphical_appearance_attribute_treatment)
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun Index.toDisplayValue(): String = when (this) {
    Index.BabySizes -> stringResource(Res.string.article_index_attribute_baby_sizes_50_98)
    Index.ChildrenAccessories -> stringResource(Res.string.article_index_attribute_children_accessories)
    Index.ChildrenSizes134_170 -> stringResource(Res.string.article_index_attribute_children_sizes_134_170)
    Index.ChildrenSizes92_140 -> stringResource(Res.string.article_index_attribute_children_sizes_92_140)
    Index.Divided -> stringResource(Res.string.article_index_attribute_divided)
    Index.LadiesAccessories -> stringResource(Res.string.article_index_attribute_ladies_accessories)
    Index.Ladieswear -> stringResource(Res.string.article_index_attribute_ladieswear)
    Index.LingeriesTights -> stringResource(Res.string.article_index_attribute_lingeries_tights)
    Index.Menswear -> stringResource(Res.string.article_index_attribute_menswear)
    Index.Sport -> stringResource(Res.string.article_index_attribute_sport)
    Index.Swimwear -> stringResource(Res.string.article_garment_group_attribute_swimwear)
    Index.Other -> stringResource(Res.string.article_color_attribute_other)
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun Shade.toDisplayValue(): String = when (this) {
    Shade.Bright -> stringResource(Res.string.article_shade_attribute_bright)
    Shade.Dark -> stringResource(Res.string.article_shade_attribute_dark)
    Shade.DustyLight -> stringResource(Res.string.article_shade_attribute_dusty_light)
    Shade.Light -> stringResource(Res.string.article_shade_attribute_light)
    Shade.Medium -> stringResource(Res.string.article_shade_attribute_medium)
    Shade.MediumDusty -> stringResource(Res.string.article_shade_attribute_medium_dusty)
    Shade.Other -> stringResource(Res.string.article_color_attribute_other)
}