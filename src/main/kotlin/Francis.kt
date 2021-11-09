import java.lang.Double.parseDouble

var accLogged:AccountModel? = null
var started:Boolean = true
var pinValid:Boolean = false
fun main(args: Array<String>){
    println(" WELCOME TO MY ATM MACHINE ")
    while (started){
        println("Please enter your 4-digit pin \t\t\tClose?[1]")
        var pin = readLine().toString()
        if(pin.isEmpty()) println("Please make sure the pin is not empty.")
        else if(pin == "1") started = false // started =false  to close the system
        else if(started){ //proceed if not closed
            pinValid = IsPinValid(pin) //check pin is valid and is on the data and sets the account logged
            println("Welcome ${accLogged?.name}")
            while(pinValid){
                println("TRANSACTION: [A]Balance [B]Deposit [C]Withdraw [D]Send [E]Cancel [F]Exit")
                val transaction = readLine().toString()
                if(transaction.isEmpty())
                    else if(transaction.lowercase() == "a"){
                        Balance()
                    }
                    else if(transaction.lowercase() == "b"){
                        Deposit()
                    }
                    else if(transaction.lowercase() == "c"){
                        Withdraw()
                    }
                    else if(transaction.lowercase() == "d"){
                        Send()
                    }
                    else if(transaction.lowercase() == "e"){
                        pinValid = false //go back to pin enter
                    }
                    else if(transaction.lowercase() == "f"){
                        println("Thanks for using my ATM MACHINE! :)")
                        pinValid = false // stop pin loop
                        started = false // stop system loop
                    }
                    else println("Please make sure the transaction is valid.")
            }
        }

        else{
            println("Thank you for my atm machine!")
        }
    }

}

// check pin validty
fun IsPinValid(pin: String):Boolean{
    var isValid = false
    var isNumber:Boolean = StringIsDigit(pin)
    if(pin.length == 4){
        if(isNumber){
            accLogged = GetAccount(pin)
            if(accLogged == null) println("Account does not exist. Please try again.")
            else isValid = true
        }
        else{
            println("Pin must be numeric.")
            isValid = false
        }
    }
    else{
        println("Pin must be 4-characters short.")
        isValid = false
    }
    return isValid
}

//check if string is number
fun StringIsDigit(value:String):Boolean{
    var isValid:Boolean = false
    isValid = try {
        val num = Integer.parseInt(value)
        true
    } catch (e: NumberFormatException) {
        false
    }
    return isValid
}
fun StringIsNumber(value:String):Boolean{
    var reg = "-?\\d+(\\.\\d+)?".toRegex()
    if(value.matches(reg))return true
    return false
}

//check if pin exists
fun GetAccount(pin:String="", accNum: String=""): AccountModel {
    var acc: AccountModel? = null
    for(account in accounts){
        //account[0] = ID
        //account[1] = Name
        //account[2] = Balance
        //account[3] = 4-digit PIN
        //check if pin exists
        if(pin != ""){
            if(account.pin == pin){
                acc = account
                break
            }
        }
        else if(accNum != ""){
            if(account.accNum == accNum){
                acc = account
                break
            }
        }
    }
    return acc!!
}

//>Main Functions<//
// cancel = go back cancels loop
// exit = set started = false and pinValid = false and transaction loop to false
fun Balance(){
    println("Balance: ${accLogged?.bal.toString()}")
}
fun Deposit(){
    while(true){
        print("Deposit amount: ")
        var input = readLine().toString()
        if(input.isEmpty())println("Input must not be empty.")
        else if(!StringIsNumber(input)) println("Input must be a number.")
        else{
            var newBal = accLogged?.bal?.plus(input.toDouble())
            if (newBal != null) {
                accounts[accounts.indexOf(accLogged)].bal = newBal
            }
            println("Deposit: ${input}\t\tNew balance: ${toCurrency(newBal.toString())}")
            break
        }
    }
}
fun Withdraw(){
    while(true) {
        print("Withdraw amount: ")
        var input = readLine().toString()
        if (input.isEmpty()) println("Input must not be empty.")
        else if (!StringIsNumber(input)) println("Input must be a number.")
        else if(accLogged?.bal!! < input.toDouble()) println("Withdraw amount must not be greater than balance.")
        else {
            var newBal = accLogged?.bal?.minus(input.toDouble())
            if (newBal != null) {
                accounts[accounts.indexOf(accLogged)].bal = newBal
            }
            println("Withdrawn: ${input}\t\tNew balance: ${toCurrency(newBal.toString())}")
            break
        }
    }
}
fun Send(){
    var rAcc: AccountModel? = null
    var accNum: String = ""
    var name: String = ""
    var amount: String = ""
    while(true) {
        println("Send Money")
        while(true){
            print("Receiver Account Number: ")
            accNum = readLine().toString()
            if (accNum.isEmpty())println("Input must not be empty.")
            else {
                rAcc = GetAccount("", accNum)
                if (rAcc == null) println("Account does not exist.")
                else if (accLogged?.accNum == accNum) println("You are not a receiver.")
                else break
            }
        }
        while(true){
            print("Receiver Name: ")
            name = readLine().toString()
            if(name.isEmpty())println("Input must not be empty.")
            else if(rAcc?.name != name) println("Account number with this name does not exist.")
            else break
        }
        while(true){
            print("Send Amount: ")
            amount = readLine().toString()
            if(amount.isEmpty())println("Input must not be empty.")
            else if(!StringIsNumber(amount)) println("Amount input must be a number.")
            else if(amount.toDouble() < 0) println("Amount input must not be less than 0.")
            else if(amount.toDouble() > accLogged?.bal!!) println("Amount must be less than or equal to your balance: ${accLogged?.bal}")
            else break
        }

        if(accNum != "" && name != "" && amount != ""){
            //send
            //deduct and add to new
            //deduct lines
            var newLoggedBal = accLogged?.bal?.minus(amount.toDouble())
            accounts[accounts.indexOf(accLogged)].bal = newLoggedBal!!
            //add lines
            var newRecieverBal = accLogged?.bal?.plus(amount.toDouble())
            accounts[accounts.indexOf(accLogged)].bal = newLoggedBal!!
            println("Sent: $amount \t\tBalance: $newLoggedBal")
            break
        }
    }
}

data class AccountModel(
    var accNum: String,
    var name: String,
    var bal: Double,
    var pin: String,
)
var accounts = listOf(
    AccountModel("123456", "Francis Hisona", 3200.32, "1234"),
    AccountModel("234567", "Kenneth Villamido", 4244.45, "0000"),
    AccountModel("332233", "God Is Good", 10093.45, "8888"),
    AccountModel("443344", "John Paul Gabule", 55552.32, "0303")
)
