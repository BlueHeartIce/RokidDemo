package com.blue.glassesapp.common.enums

/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/7</p>
 *
 * 业务类型
 * 1. 识别人员身份信息
 */
enum class BusinessType(val value: String, val desc: String) {
    RECOGNIZE_PERSON_IDENTITY("RECOGNIZE_PERSON_IDENTITY", "识别人员身份信息"),
    RECOGNIZE_PERSON_FACE("RECOGNIZE_PERSON_CHECK_FACE", "人员通行核验"),
}