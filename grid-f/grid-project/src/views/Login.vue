<template>
    <div class="container">
        <div class="left">
            <div class="login">
                <div id="title">
                    <h1 >Welcome to Grid!!</h1>
                </div>
                <div id="title-content">
                    Log in to streamline your business processes.
                </div>
                Have a great day!
                <div class="email">
                    <div>
                        Work Email
                    </div>
                    <div>

                    </div>
                </div>
                <div class="password">
                    
                </div>
                <div class="button">

                </div>
                <div class="line">

                </div>
                <div class="find-id">

                </div>
                <div class="find-pwd">

                </div>
            </div>
        </div>
        <div class="right">
        
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped>
.container {

    height: 100vh;
    width: 100%;
    display: flex;
}

.left {
    width: 75%;
    display: flex;
    justify-content: center;
}

.login { 
    margin: auto;
    background-color: red;
    height: 70%;
    width: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.email { 
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.password {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.button {

}

.line {

}

.find-id {

}

.find-pwd {

}

.right {
    background-color: #002366;
    width: 25%;
}

#title { 
    font-size: 1em;
}

#title-content { 

}



</style>