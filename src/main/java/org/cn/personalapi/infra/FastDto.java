package org.cn.personalapi.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FastDto {
    public record Fashion(
            String productId,
            String brand,
            String name,
            Integer finalPrice,
            Integer originalPrice,
            Integer bestPrice,
            Integer discountRate,
            Integer rank,
            String imageUrl,
            String productUrl,
            String watchingText
    ) {}

    public record Beauty(
            @JsonProperty("product_id") String productId,
            @JsonProperty("name") String name,
            @JsonProperty("product_url") String productUrl,
            @JsonProperty("image_url") String imageUrl,
            @JsonProperty("final_price") Integer finalPrice,
            @JsonProperty("original_price") Integer originalPrice,
            @JsonProperty("coupon_price") Integer couponPrice,
            @JsonProperty("discount_rate") Integer discountRate,
            @JsonProperty("coupon_discount_rate") Integer couponDiscountRate,
            @JsonProperty("brand") String brand,
            @JsonProperty("brand_name") String brandName,
            @JsonProperty("brand_url") String brandUrl,
            @JsonProperty("gender") String gender,
            @JsonProperty("sold_out") Boolean soldOut,
            @JsonProperty("review_count") Integer reviewCount,
            @JsonProperty("review_score") Integer reviewScore,
            @JsonProperty("is_option_visible") Boolean isOptionVisible,
            @JsonProperty("options") List<Object> options
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true) // images 같은 추가 필드 무시
    public record Review(

            @JsonProperty("review_id")
            Long reviewId,

            @JsonProperty("type")
            String type,

            // JSON이 "3" 이라서 String 으로 받는 게 안전
            @JsonProperty("grade")
            String grade,

            @JsonProperty("content")
            String content,

            @JsonProperty("comment_count")
            Integer commentCount,

            @JsonProperty("like_count")
            Integer likeCount,

            @JsonProperty("user_name")
            String userName,

            @JsonProperty("user_profile")
            String userProfile,

            @JsonProperty("user_image")
            String userImage,

            @JsonProperty("has_skin_worry")
            Boolean hasSkinWorry,

            @JsonProperty("show_user_profile")
            Boolean showUserProfile,

            @JsonProperty("goods_no")
            Long goodsNo,

            @JsonProperty("goods_name")
            String goodsName,

            @JsonProperty("goods_option")
            String goodsOption,

            @JsonProperty("goods_brand")
            String goodsBrand,

            @JsonProperty("goods_thumbnail")
            String goodsThumbnail,

            @JsonProperty("created_at")
            String createdAt,

            @JsonProperty("experience")
            Boolean experience,

            @JsonProperty("my_review")
            Boolean myReview
    ) {}


    public record Option(

            @JsonProperty("group_no")
            String groupNo,

            @JsonProperty("group_name")
            String groupName,

            @JsonProperty("group_type")
            String groupType,

            @JsonProperty("display_type")
            String displayType,

            @JsonProperty("value_no")
            String valueNo,

            @JsonProperty("option_no")
            String optionNo,

            @JsonProperty("name")
            String name,

            @JsonProperty("code")
            String code,

            @JsonProperty("sequence")
            String sequence,

            @JsonProperty("color_code")
            String colorCode,

            @JsonProperty("color_type")
            String colorType,

            @JsonProperty("image_url")
            String imageUrl,

            @JsonProperty("is_deleted")
            Boolean isDeleted,

            @JsonProperty("is_leaf")
            Boolean isLeaf,

            @JsonProperty("out_of_stock")
            Boolean outOfStock
    ) {}

}
