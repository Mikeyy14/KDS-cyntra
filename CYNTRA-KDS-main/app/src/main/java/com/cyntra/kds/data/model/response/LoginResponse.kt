package com.cyntra.kds.data.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val status: Boolean
)

data class LoginData(
    val access: Access,
    val access_store: List<Any>,
    val auth_token: String,
    val business_date: String,
    val device_info: DeviceInfo,
    val employee: Employee,
    val gateways_info: GatewaysInfo,
    val password_reset: Int,
    val password_reset_days: Int,
    val server_time: Long,
    val session_id: String,
    val store_details: StoreDetails,
    val store_session_id: String,
    val store_status: String,
    val is_tax_included:Boolean
)

data class StoreDetails(
    val _id: String,
    val email_id: String,
    val is_email_acitve: Boolean,
    val is_sms_active: Boolean,
    val parent_store_id: String,
    val status: Boolean,
    val store_ad1: String,
    val store_ad2: String,
    val store_city: String,
    val store_contact_no: String,
    val store_country: String,
    val store_currency: String,
    val store_id: String,
    val store_industry: String,
    val store_logo: String,
    val store_name: String,
    val store_org_name: String,
    val store_owner_name: String,
    val store_plan: String,
    val store_state: String,
    val store_tagline: String,
    val store_timezone: String,
    val store_type: String,
    val store_zip: String,
    val subscription_end_date: String,
    val subscription_start_date: String
)


data class DeviceInfo(
    val _id: String,
    val device_id: String,
    val mosambee_password: String,
    val mosambee_user_id: String,
    val pilot: Boolean,
    val store_id: String,
    val terminal_id: String
)

data class Employee(
    val _id: String,
    val action: String,
    val created_at: String,
    val emp_doj: String,
    val emp_email_id: String,
    val emp_fn: String,
    val emp_fnm: String,
    val emp_lnm: String,
    val emp_mobile_no: String,
    val emp_name: String,
    val emp_no: String,
    val password: String,
    val password_expiry: String,
    val position: String,
    val primary_group: String,
    val status: Boolean,
    val store_id: String,
    val updated_at: String
)


data class Access(
    val AcceptExpiredStoreCredit: Boolean,
    val AcceptLostStoreCredit: Boolean,
    val AcceptTillCountDiscrepancy: Boolean,
    val AccessOtherTills: Boolean,
    val AddCommentinJournal: Boolean,
    val AddCustomertoSale: Boolean,
    val AddDiscount: Boolean,
    @SerializedName("AdministerOperatingSystem(grantscommandpromptaccess)")
    val AdministerOperatingSystem: Boolean,
    val AdministrativeWorkCode: Boolean,
    val AllowEmployeeSale: Boolean,
    val AllowExpiredCouponOverride: Boolean,
    val AllowForeignChangeAboveAvailableFunds: Boolean,
    val AllowInactiveCouponOverride: Boolean,
    val AssociateAdvance: Boolean,
    val AssociateMaintenance: Boolean,
    val AttachCashDrawerTill: Boolean,
    val AttachOtherTill: Boolean,
    val AttachTill: Boolean,
    val BackofficeAccess: Boolean,
    val BlindReturnsThresholdAmount: Boolean,
    val CancelLayaway: Boolean,
    val CancelTransaction: Boolean,
    val CannotBeLockedOut: Boolean,
    val CashChangeDueLimit: Boolean,
    val CashPickup: Boolean,
    val CashTransfer: Boolean,
    val ChangeDiscount: Boolean,
    val ChangePasswordofOtherUser: Boolean,
    val ChangePrice: Boolean,
    val ChangePriceonVerifiedReturn: Boolean,
    val ChangeQuantity: Boolean,
    val ChangeTillFloat: Boolean,
    val CheckforUpdates: Boolean,
    val CloseStorewithSuspendedSales: Boolean,
    val CreateCustomer: Boolean,
    val CustomernotpresenttenderManagerOverride: Boolean,
    val EndCountOtherTill: Boolean,
    val ExceedMaximumDiscount: Boolean,
    val ForManulDiscount: Boolean,
    val ForPriceOverride: Boolean,
    val ForceCloseReplenishment: Boolean,
    val ForceRedemptionofCoupon: Boolean,
    val IssueInvoiceFromEJ: Boolean,
    val IssueTill: Boolean,
    val KeepCashDrawerOpen: Boolean,
    val LayawayItemPriceBelowMinimum: Boolean,
    val Login: Boolean,
    val LogintoSystem: Boolean,
    val LogintoXenvironment: Boolean,
    val MaintainCurrencyExchangeRates: Boolean,
    val ManageNetworkedCashDrawers: Boolean,
    val ManageTills: Boolean,
    val ManualOverrideforAccountReceivable: Boolean,
    val ManulEntryforPointRedeemption: Boolean,
    val MobileClientDeviceAccess: Boolean,
    val MposDashboard: Boolean,
    val NeedtocomefromtheTenderModetoItemMode: Boolean,
    val NoSale: Boolean,
    val OTPFailureaskedforManageroverride: Boolean,
    val OpenStoreBank: Boolean,
    val OverrideBlindReturnPrice: Boolean,
    val OverrideCancelLayawaytoEscrow: Boolean,
    val OverrideCancelSpecialOrdertoEscrow: Boolean,
    val OverrideLayawayDeposit: Boolean,
    val OverrideMaximumEnteredReceivingQuantity: Boolean,
    val OverrideOnHoldDepositAmount: Boolean,
    val OverrideforEnteringFlightInformation: Boolean,
    val OvertenderAboveMaximum: Boolean,
    @SerializedName("PerformtheEnd-Of-Dayprocess")
    val PerformtheEndOfDayprocess: Boolean,
    val PostVoidTransactioninBackOffice: Boolean,
    val PostVoidTransactioninRegister: Boolean,
    val PostVoidanInvoiceFromEJ: Boolean,
    val PostVoidfromJournal: Boolean,
    val PostVoidshouldbedoneinPOS: Boolean,
    val PrintCustomerDetails: Boolean,
    val ReconcileStoreBank: Boolean,
    val ReconcileTill: Boolean,
    val Reconciletillsessionincashdraweraccountability: Boolean,
    val Reconciletillsessioninregisteraccountability: Boolean,
    val RefundMoreCashThanAvailable: Boolean,
    val RefundTenderBalance: Boolean,
    val ReloadGiftCard: Boolean,
    val RemoveCashDrawerTill: Boolean,
    val RemoveOtherTill: Boolean,
    val RemoveOwnTill: Boolean,
    val ReprintInvoiceFromEJ: Boolean,
    val ReprintLastInvoice: Boolean,
    val ReprintReceiptfromJournal: Boolean,
    val ReprintReceiptfromPreviousDay: Boolean,
    val ReturnGiftCard: Boolean,
    val ReturnItem: Boolean,
    val ReturnItemNotinOriginalSale: Boolean,
    val ReturnItemOverMaximumPrice: Boolean,
    val ReturnItemUnderMinimumPrice: Boolean,
    val ReturnMoreThanQuantityPurchased: Boolean,
    @SerializedName("ReturnNon-MerchandiseItem")
    val ReturnNonMerchandiseItem: Boolean,
    val ReturnTill: Boolean,
    val ReturnUnreturnableItem: Boolean,
    val ReturnUnreturnableItemAfterMaxDays: Boolean,
    val SellGiftCard: Boolean,
    val SellGiftCertificate: Boolean,
    val SellItem: Boolean,
    val SellItemNotonFile: Boolean,
    @SerializedName("SellNon-MerchandiseItem")
    val SellNonMerchandiseItem: Boolean,
    @SerializedName("StartofDay/EndofDay")
    val StartofDayEndofDay: Boolean,
    val Stockfilegeneration: Boolean,
    val StoreBankCashDeposit: Boolean,
    val SuspendTransaction: Boolean,
    val TILLOption: Boolean,
    val TenderAboveAllowedAmount: Boolean,
    val TransactionManualDiscountLimit: Boolean,
    val UnlockRegister: Boolean,
    val ViewCustomerDetails: Boolean,
    val VoidLineItem: Boolean,
    val VoidTender: Boolean,
    val XfgShowcodefunctions: Boolean,
    val XfgVoidthelastitemsold: Boolean
)

class GatewaysInfo