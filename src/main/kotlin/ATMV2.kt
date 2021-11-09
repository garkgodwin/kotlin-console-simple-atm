import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

//------------------MODELS
data class Transaction(
    var date: String,
    var amount: Double,
    var transactionType: String,
)
data class AtmAccount(
    var pin:String,
    var accountNumber: String,
    var name: String,
    var balance: Double,
    var address: String,
    var transactions: ArrayList<Transaction>,
)


//------------------DATA
var data = listOf(
    AtmAccount("1111",
        "12345678",
        "Gark Godwin Duque",
        9999.00,
        "Maramag",
        arrayListOf(
            Transaction("02/12/20 11am", 3000.00, "WITHDRAW"),
            Transaction("01/13/32 10am", 2000.00, "DEPOSIT"),
            Transaction("02/12/20 11pm", 2500.00, "WITHDRAW"),
        )
    ),
    AtmAccount("2222",
        "23456789",
        "John Paul Gabule",
        23004.39,
        "Valencia",
        arrayListOf(
            Transaction("02/12/20 11am", 3000.00, "DEPOSIT"),
            Transaction("01/13/32 10am", 2000.00, "WITHDRAW"),
            Transaction("02/12/20 11pm", 2500.00, "DEPOSIT"),
        )
    ),
)
//-------------Helpers
open class SuperHelpers(){
    fun inputIsEmpty(value:String):Boolean{
        if(value == null || value == "" || value.isEmpty()){
            return true
        }
        return false
    }
    fun pinIsDigitsOnly(pin:String):Boolean{
        val pattern: Regex= "^[0-9]+$".toRegex()
        if(!pattern.matches(pin)){
            return false
        }
        return true
    }
    fun pinIsFourCharsOnly(pin:String):Boolean{
        if(pin.length != 4)return false
        return true
    }
    fun getAccount(pin:String="", accountNumber: String=""):AtmAccount?{
        var account:AtmAccount? = null
        for(a in data){
            if(pin != ""){
                if(a.pin == pin){
                    account = a
                    break
                }
            }
            else if(accountNumber != ""){
                if(a.accountNumber == accountNumber){
                    account = a
                    break
                }
            }
        }
        return account
    }
    fun compareAccounts(reciever: AtmAccount, sender: AtmAccount){

    }
    fun isNumber(value: String):Boolean{
        val numberRegex = "-?\\d+(\\.\\d+)?".toRegex()
        if(!value.matches(numberRegex)){
            return false;
        }
        return true;
    }
    fun toCurrency(value: String): String {
        var formatter: NumberFormat = DecimalFormat("#,###.####")
        var myNumber = value.toDouble()
        return formatter.format(myNumber)
    }
    fun balanceIsGreaterOrEqual(balance:Double, amount:Double):Boolean{
        if(balance >= amount)return true
        return false
    }
    fun isMinimumDeposit(value:Double):Boolean{
        return (value >= 100)
    }
    fun isMinimumSend(value:Double):Boolean{
        return (value>= 20)
    }
}
class ATM():SuperHelpers()  {
    var pin:String = ""
    var account:AtmAccount? = null
    //---------------------MAIN FUNCTIONS
    fun login():String{
        println("Please enter your pin\t\t\t| [1]Go back | [2]Exit System")
        var pin = readLine().toString()
        val inputIsEmpty = inputIsEmpty(pin)
        if(inputIsEmpty){
            println("Input must not be empty.")
            return "INVALID"
        }
        if(pin == "1"){
            println("Going back to main.")
            return "BACK"
        }
        if(pin == "2"){
            println("System is closing...")
            return "EXIT"
        }
        //check if pin is valid if not
        if(pinIsDigitsOnly(pin)){
            return if(pinIsFourCharsOnly(pin)){
                var account = getAccount(pin, "")
                if(account != null){
                    this.account = account
                    this.pin = pin
                    println("Welcome ${account?.name}")
                    "VALID"
                }else{
                    println("Your pin number is invalid.")
                    "INVALID"
                }
            } else{
                println("Pin must be 4 characters short.")
                "INVALID"
            }
        }
        else{
            println("Pin must be numeric digits only.")
            return "INVALID"
        }
    }
    fun deposit():String {
        var result:String = ""
        println("Please enter your desired amount to deposit\t\t\t | [C]Cancel | [E]Exit System")
        val amount = readLine().toString()
        val inputIsEmpty = inputIsEmpty(amount)
        if (inputIsEmpty) {
            println("Deposit amount must not be empty.")
            result = "INVALID"
        }
        else if(amount.lowercase() == "c") result = "CANCEL"
        else if(amount.lowercase() == "e") result = "EXIT"
        else if(!isNumber(amount)){
            println("Deposit input must be a number.")
            result = "INVALID"
        }
        else {
            //deposit here
            for(account in data){
                if(account.pin == pin){
                    val newValue = account.balance+amount.toDouble()
                    data[data.indexOf(account)].balance = newValue
                    var dateFormat = SimpleDateFormat("MM/dd/yy hh aa")
                    val current = dateFormat.format(Date())
                    var transaction = Transaction(current, amount.toDouble(), "DEPOSIT")
                    data[data.indexOf(account)].transactions.add(transaction)
                    println("You deposited ₱${toCurrency(amount)} and the current balance is ₱${toCurrency(newValue.toString())}")
                    break
                }
            }
            result = "VALID"
        }
        return result
    }
    fun withdraw():String{
        var result:String = ""
        println("Please enter your desired amount to withdraw\t\t\t | [C]Cancel | [E]Exit System")
        val amount = readLine().toString()
        val inputIsEmpty = inputIsEmpty(amount)
        if (inputIsEmpty){
            println("Withdraw amount must not be empty.")
            result = "INVALID"
        }
        else if(amount.lowercase() == "c") result =  "CANCEL"
        else if(amount.lowercase() == "e") result = "EXIT"
        else if(!isNumber(amount)){
            println("Withdraw input must be a number.")
            result = "INVALID"
        }
        else{
            //withdraw here
            for(account in data){
                if(account.pin == pin){
                    var balIsGreater = balanceIsGreaterOrEqual(account.balance, amount.toDouble())
                    if(balIsGreater){
                        val newValue = account.balance-amount.toDouble()
                        data[data.indexOf(account)].balance = newValue
                        var dateFormat = SimpleDateFormat("MM/dd/yy hh aa")
                        val current = dateFormat.format(Date())
                        var transaction = Transaction(current, amount.toDouble(), "WITHDRAW")
                        data[data.indexOf(account)].transactions.add(transaction)
                        println("You withdrawn ₱${toCurrency(amount)} and the current balance is ₱${toCurrency(newValue.toString())}")
                        result = "VALID"
                        break
                    }
                    else{
                        println("Balance must be greater or equal to withdraw amount.")
                        result = "INVALID"
                        break //breaks for loop to stop looping whole data in an account
                    }
                }
            }
        }
        return result
    }
    fun send():String{
        var result: String = ""
        //VARIABLES FOR SENDING MONEY
        var account: AtmAccount? = null
        var accountNumber:String = ""
        var name:String = ""
        var amount:Double = 0.0
        println("Please complete the following\t\t\t | [C]Cancel | [E]Exit System")
        println("==Receiver Data==")
        //! LOOP ALL INPUTS TO CHECK VALIDITY
        while(true){ //LOOPING ACCOUNT NUMBER
            print("Account Number: ")
            val input = readLine().toString()
            var inputIsValid = !inputIsEmpty(input)
            if(inputIsValid){
                if(input.lowercase() == "c") {
                    result = "CANCEL"
                    break
                }
                else if(input.lowercase() == "e") {
                    result = "EXIT"
                    break
                }
                else{
                    account = getAccount("", input)
                    if(account!=null) {
                        result = "" // reset result since input is now valid
                        accountNumber = input
                        break
                    }
                    else println("Account number was not found. Please try again.")
                }
            }
            else{
                println("Account number must not be empty. Please try again.")
            }
        }//LOOPING ACCOUNT NUMBER
        while(result==""){ //LOOPING NAME
            var result:String = ""
            print("Name(First Middle Last): ")
            val input = readLine().toString()
            var inputIsValid = !inputIsEmpty(input)
            if(inputIsValid){
                if(input.lowercase() == "c") {
                    result = "CANCEL"
                    break
                }
                else if(input.lowercase() == "e") {
                    result = "EXIT"
                    break
                }
                else{
                    if (account != null) {
                        if(input.lowercase() == account.name.lowercase()) {
                            result = "" // reset result since input is now valid
                            name = input
                            break
                        }else println("Receiver's name with this account number does not match. Please try again.")
                    }
                    else println("Receiver account was not found.")
                }
            }
            else println("Name must not be empty. Please try again.")
        }//LOOPING NAME
        while(result =="") { //LOOPING AMOUNT TO SEND
            print("Amount to send(Minimum Send Amount is ₱20): ")
            val input = readLine().toString()
            var inputIsValid = !inputIsEmpty(input)
            if(inputIsValid){
                if(input.lowercase() == "c"){
                    result = "CANCEL"
                    break
                }
                else if(input.lowercase() == "e"){
                    result = "EXIT"
                    break
                }
                else{
                    if(isNumber(input)){
                        if(isMinimumSend(input.toDouble())){
                            result = "" // reset result since input is now valid
                            amount = input.toDouble()
                            break
                        }else println("Amount to send must have a minimum value of ₱20")
                    }else println("Amount to send must be a number. Please try again")
                }
            }else println("Amount to send must not be empty. Please try again.")
        } //LOOPING AMOUNT TO SEND
        return result
        //TODO: MAKE THE SEND NOW
        //TODO: !IMPORANT CHECK FIRST IF ACCOUNT NUMBER IS NOT EQUAL TO LOGGED IN ACCOUNT
    }
    fun balance(){
        println("You have chosen balance")
    }
    fun transactionHistory(){
        println("You have chosen transaction history")
    }
    fun closeAccount(){
        println("You have chosen close account")
    }

    //outside islogged
    fun retrieveClosedAccount(){
        println("You have chosen retrieve closed account")
    }
    fun createAccount(){
        //minimum of 1000
        println("You have chosen create account")
    }

}

var systemStarted = true
var authStarted = false
fun main(args: Array<String>){
    var atm:ATM = ATM()
    println("Welcome to the ATM version 2 peace!")
    while(systemStarted){
        println("[1] Enter pin | [2] Create Account | [3] Retrieve Account | [4] Exit System")
        var action = readLine().toString()
        var actionIsValid = !atm.inputIsEmpty(action)
        if(actionIsValid){
            when(action){
                "1" -> authStarted = true
                "2" -> atm.createAccount()
                "3" -> atm.retrieveClosedAccount()
                "4" -> {
                    systemStarted = false
                    println("Closing the system.")
                }
            }
        }
        else println("Please choose a valid action.")
        while(authStarted){
            var loginResult = atm.login()
            when(loginResult){
                "BACK" -> authStarted = false
                "EXIT" -> {
                    authStarted = false
                    systemStarted = false
                }
                "VALID" -> {
                    //can now use authed actions
                    authStarted = false
                }
            }
        }

        while(atm.account != null){
            println(
                "| [1]Deposit | [2]Withdraw | [3]Send | [4]Balance | [5]Transactions " +
                        "| [6]Close Account | [7]Go back | [8]Exit System |"
            )
            var action = readLine().toString()
            var actionIsValid = !atm.inputIsEmpty(action)
            if (actionIsValid) {
                var onTransaction = false
                when (action) {
                    "1" -> {
                        onTransaction=true
                        while(onTransaction){
                            var depositResult = atm.deposit()
                            println("DEPOSIT RESULT: ${depositResult}")
                            if(depositResult == "VALID" || depositResult == "CANCEL")onTransaction = false
                            if(depositResult=="EXIT") {
                                onTransaction = false
                                systemStarted = false
                                atm.account = null
                                atm.pin = ""
                            }
                        }

                    }
                    "2" -> {
                        while(true){
                            var withdrawResult = atm.withdraw()
                            if(withdrawResult == "VALID" || withdrawResult == "CANCEL") break
                            if(withdrawResult == "EXIT"){
                                systemStarted = false
                                atm.account = null
                                atm.pin = ""
                            }
                        }
                    }
                    "3" -> {
                        while(true) {
                            var sendResult = atm.send()
                            if(sendResult == "VALID" || sendResult == "CANCEL")break
                            if(sendResult == "EXIT"){
                                systemStarted = false
                                atm.account = null
                                atm.pin = ""
                            }
                        }
                    }
                    "4" -> atm.balance()
                    "5" -> atm.transactionHistory()
                    "6" -> atm.closeAccount()
                    "7" -> {
                        println("Going back...")
                        atm.pin = ""
                        atm.account = null
                    }
                    "8" -> {
                        println("Closing the system")
                        atm.pin = ""
                        atm.account = null
                        systemStarted = false
                    }
                }
            } else println("Please choose a valid action.")
        }
    }
}
