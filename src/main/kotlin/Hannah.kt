var accountHolderList = listOf(
arrayOf("Hannah", "9128", "1111", "1000.0"),
arrayOf("Leigh", "6535", "2222", "2000.0"),
arrayOf("Anne", "3632", "3333", "3000.0")
)


fun main(args: Array<String>) {
    val welcome = "======== WELCOME TO BASIC ATM ========"
    println(welcome)
    //validate pin
    //check if pin exists
    var isPinValid = false
    while(!isPinValid){
        println("Enter pin number: ")
        var pin = readLine().toString()
        isPinValid = pinIsValid(pin) //pin check if exists
        var onAction = true
        while (onAction && isPinValid) {
            chooseAction(pin)
            print("Would you like to make another transaction? [Any Key - Yes] [1 - NO]")
            var response = readLine().toString()
            if (response == "1"){
                onAction = false
            }
        }
    }

/////////// NEED TO FIX
}

fun pinIsValid(pin:String):Boolean{
    for(a in accountHolderList){
        if(a[1] == pin){
            return true
        }
    }
    return false
}

fun amountIsValid(amount:String):Boolean{
    var isValid = false
    if(stringIsNotEmpty(amount)){
        //valid
        isValid = if(amountIsNumber(amount)){
            true
        } else{
            println("Must be a number")
            false
        }
        return isValid
    }
    else{
        println("Cannot be empty")
        isValid = false
    }
    return isValid
}
fun stringIsNotEmpty(amount: String):Boolean{
    if(amount == ""){
        return false
    }
    return true
}
fun amountIsNumber(amount: String):Boolean{
    val numberRegex = "-?\\d+(\\.\\d+)?".toRegex()
    if(!amount.matches(numberRegex)){
        return false;
    }
    return true;
}
// ========== CHOOSE ACTION ==========

fun chooseAction(pin: String){
    val string = "[1 - Check Current Balance] " +
            " [2 - Deposit]" + " [3 - Withdraw]" +
            " [4 - Transfer Money] "

    print(string)
    var action = readLine().toString()
    when(action){
        "1" -> retrieve_balance(pin)
        "2" -> deposit(pin)
        "3" -> withdraw(pin)
        "4" -> transferMoney(pin)
        else -> chooseAction(pin)
    }

    println("")
}


// ========== RETRIEVE BALANCE ==========
fun retrieve_balance(pin: String){
    for(a in accountHolderList){
        if (pin == a[1]){
            print("${a[3]} ")
        }
    }
}


// =========== DEPOSIT  ===========

fun deposit(pin: String) {
    var bool = true
    while(bool){
        print("Enter amount: ")
        val amount = readLine().toString()
        var isValid = amountIsValid(amount)
        if(isValid){
            for (a in accountHolderList) {
                if (pin == a[1]) {
                    var value = a[3].toDouble() + amount.toDouble()
                    a.set(3, value.toString())
                    println("The amount of ₱ $amount is successfully added to your current balance." +
                            " Your current balance is ₱ ${a[3]}")
                    bool = false
                }
            }
        }
    }
}

// =========== WITHDRAW  ===========

fun withdraw(pin: String) {
    var bool = true
    while(bool){
        print("Enter amount: ")
        val amount = readLine().toString()
        var isValid = amountIsValid(amount)
        if(isValid){
            for (a in accountHolderList) {
                if (pin == a[1]) {
                    if (amount.toDouble() >= a[3].toDouble())
                    {
                        println("Insufficient Balance. Try again!")
                        withdraw(pin)
                    }
                    else{
                        var value = a[3].toDouble() + amount.toDouble()
                        a.set(3, value.toString())
                        println("The amount of ₱ $amount is successfully added to your current balance." +
                                " Your current balance is ₱ ${a[3]}")
                        bool = false
                    }
                }
            }
        }
    }
}

// =========== TRANSFER  ===========

fun transferMoney(pin: String) {
    var bool = true
    while (bool) {
        //values
        println("Transfer Money [X - Cancel Transaction]")
        var accName: String = ""
        var accNum: String = ""
        var amount: String = ""

        //booleans
        var isValid = false
        while (!isValid) {

            //CHECK IF SELF OR NOT


            //CHECK IF ACCOUNT NAME IS VALID OR EXISTS
            var isAccountNameValid = false
            while(!isAccountNameValid){
                print("Enter Account Holder's Name: ")
                accName = readLine().toString()
                isValid = stringIsNotEmpty(accName)
                if (accName == "X" || accName == "x") {
                    isValid = true
                    bool = false
                }
                //check if name exists
                if (isValid) {
                    isValid = accountNameExists(accName)
                }
                isAccountNameValid = isValid
            }

            //CHECK ACCOUNT NUMBER IS VALID OR EXISTS
            var isAccNumValid = false
            while(!isAccNumValid && isValid){
                if (isValid) {
                    print("Enter Account Number: ")
                    accNum = readLine().toString()
                    isValid = stringIsNotEmpty(accNum)
                    if (accNum == "X" || accNum == "x") {
                        isValid = true
                        bool = false
                    }
                }
                //check if account number exists
                if (isValid) {
                    isValid = accountNumberExists(accNum)
                }
                isAccNumValid = isValid
            }

            //----CHECK IF ACCOUNT NUMBER AND ACCOUNT NAME IS SELF
            if(isValid){
                isValid = !isSelf(accName, accNum, pin) // if isSelf is true then cannot send money promt
            }


            //check if amount is valid
            var isAmountValid = false
            while(!isAmountValid && isValid){
                if (isValid) {
                    print("Enter amount:")
                    amount = readLine().toString()
                    isValid = stringIsNotEmpty(amount)
                    if (isValid) {
                        if (amount == "X" || amount == "x") {
                            isValid = true
                            bool = false
                        } else {
                            isValid = amountIsNumber(amount)
                        }
                    }
                }
                isAmountValid = isValid
            }



            //if transaction continue and inputs are valid
            if (bool && isValid) {
                var currentBal: Double = 0.0;
                for (a in accountHolderList) {
                    if (a[0] == accName && a[2] == accNum) {
                        //add sa reciever
                        var totalAmount = a[3].toDouble() + amount.toDouble()
                        a[3] = totalAmount.toString()
                    }
                    //deduct amount from sender if reciever exists
                    if (a[1] == pin) {
                        //deduct sa sender
                        currentBal = a[3].toDouble() - amount.toDouble()
                        a[3] = currentBal.toString()
                        bool = false
                    }
                }
                println("You have transferred the amount of $amount to $accName. Your current balance is: $currentBal")
            }
            //else stop transaction or restart transaction
        }
    }
}
fun accountNameExists(key: String):Boolean{
    for(acc in accountHolderList){
        if(acc[0] == key){
            return true
        }
    }
    println("ACCOUNT NAME DOES NOT EXIST. Please try again")
    return false
}
fun accountNumberExists(key: String):Boolean{
    for(acc in accountHolderList){
        if(acc[2] == key){
            return true
        }
    }
    println("ACCOUNT NUMBER DOES NOT EXIST")
    return false
}

fun isSelf(name: String, num: String, pin:String):Boolean{
    for(acc in accountHolderList){
        if(acc[1] == pin){
            //sender data
            if(acc[0] == name && acc[2] == num){
                println("CANNOT SEND MONEY TO SELF. TRY AGAIN")
                return true
            }
        }
    }
    return false
}