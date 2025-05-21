package com.cyntra.kds.printer

import android.content.Context
import android.os.RemoteException
import com.cyntra.kds.constants.ORDER_NUMBER
import com.cyntra.kds.constants.SELECTED_ORDER_TYPE
import javax.inject.Inject

class SunMiPrinter @Inject constructor(
    val context: Context
) {

    companion object {
        const val TAG = "SunMiPrinter.kt"
        const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        var invoiceText = ""
        var kotTextForEpson = ""
        var userImage = ""


    }

    // ========================================     PAYTM PRINTER  ============================================
    @Throws(RemoteException::class)
    fun sunMiPrint(): String {
        invoiceText = ""
        val orderDetail = "com.cyntra.voicebreeze.data.model.DataController.getOrder()"

        var customerName = ""
        var customerNumber =""

        val totalItem = "orderDetail.itemCount.toInt().toString()"
        val orderType = SELECTED_ORDER_TYPE
        val billNo =  ORDER_NUMBER

        val size = 35f
        val size2 = 25f
        val size3 = 22f


        try {
                SunmiPrintHelper.getInstance().printText("\n", size, false, false, "test.ttf")

                SunmiPrintHelper.getInstance().printText(printItemNameQtyMRPThreeColumnForSunmi("","Breez Voice", "",5,12,5 ),  size, true, false, "test.ttf")
            SunmiPrintHelper.getInstance().printText("\n", size, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                .printText(printItemNameQtyMRPThreeColumnForSunmi("","New Jersey", "",8,10,5 ), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("---------------------------------", size3, false, false, "test.ttf")
            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")


                   SunmiPrintHelper.getInstance()
                       .printText(printItemNameQtyMRPThreeColumnForSunmi("Name","", customerName,12,5,10 ), size2, false, false, "test.ttf")

                SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

                SunmiPrintHelper.getInstance()
                       .printText(printItemNameQtyMRPThreeColumnForSunmi("Phone","", customerNumber,12,5,10 ), size2, false, false, "test.ttf")



            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                .printText(printItemNameQtyMRPThreeColumnForSunmi("Total Item","", totalItem,12,5,10 ), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                .printText(printItemNameQtyMRPThreeColumnForSunmi("Order Type","",orderType,12,5,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                .printText(printItemNameQtyMRPThreeColumnForSunmi("Order Number","",billNo,12,5,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                    .printText("---------------------------------", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                    .printText(printItemNameQtyMRPThreeColumnForSunmi("Qty","Name","Price", 5,13,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance()
                    .printText("---------------------------------", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")


//            for (item in orderDetail.itemDetails){
//                SunmiPrintHelper.getInstance()
//                    .printText(printItemNameQtyMRPThreeColumnForSunmi("${item.quantity.toInt()}"," ${item.itemName}",
//                            " ${"$" + formatDoublePriceString(item.extendedAmount * item.quantity)}",3,13,10), size2, false, false, "test.ttf")
//                SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")
//
//            }

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")


            SunmiPrintHelper.getInstance()
                    .printText("---------------------------------", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")


//            SunmiPrintHelper.getInstance()
//                .printText(printItemNameQtyMRPThreeColumnForSunmi("SUBTOTAL","",
//                    "$" + formatDoublePriceString(orderDetail.totalNetSale),12,5,10),size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

//            SunmiPrintHelper.getInstance()
//                .printText(printItemNameQtyMRPThreeColumnForSunmi("TAX","",
//                    "$" + formatDoublePriceString(orderDetail.totalItemTax),12,5,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

//            SunmiPrintHelper.getInstance()
//                .printText(printItemNameQtyMRPThreeColumnForSunmi("DISCOUNT","",
//                    "$" + formatDoublePriceString(orderDetail.totalDiscount),12,5,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

//            SunmiPrintHelper.getInstance()
//                .printText(printItemNameQtyMRPThreeColumnForSunmi("TOTAL","",
//                    "$" + formatDoublePriceString(orderDetail.transactionGrandAmount),12,5,10), size2, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size3, false, false, "test.ttf")

                SunmiPrintHelper.getInstance()
                    .printText("--------------------------------", size3, false, false, "test.ttf")

            SunmiPrintHelper.getInstance().printText("\n", size2, false, false, "test.ttf")


                SunmiPrintHelper.getInstance().feedPaper()
                SunmiPrintHelper.getInstance().cutpaper()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return invoiceText
    }


    private fun printItemNameQtyMRPThreeColumnForSunmi(
        text1: String,
        text2: String,
        text3: String,
        width1: Int,
        width2: Int,
        width3: Int
    ): String {
        return String.format(
            "%-" + width1 + "s",
            if (text1.length > width1) text1.substring(0, width1)
            else text1
        ) + String.format(
            "%-" + width2 + "s",
            if (text2.length > width2) text2.substring(0, width2)
            else text2
        ) + String.format(
            "%" + width3 + "s",
            if (text3.length > width3) text3.substring(0, width3)
            else text3
        )
    }







}