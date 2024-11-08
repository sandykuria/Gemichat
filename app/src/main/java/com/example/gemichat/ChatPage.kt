package com.example.gemichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gemichat.ui.theme.ColorModelMessage
import com.example.gemichat.ui.theme.ColorUserMessage
import com.example.gemichat.ui.theme.Purple80
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

//Displays the entire chat page layout
@Composable
fun ChatPage (modifier: Modifier = Modifier,viewModel: ChatViewModel){
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ){
        AppHeader()
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMesssageSend = {
            viewModel.sendMessage(it)
            }
        )
    }
}

//Displays list of messages in a scrollable column
@Composable
fun MessageList(modifier: Modifier = Modifier,messageList : List<MessageModel>){
    if (messageList.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Icon",
                tint = Purple80,
            )
            Text(text = "Ask me anything", fontSize = 22.sp)  //Instructional text
        }
    }else {
        LazyColumn(
            modifier = Modifier,
            reverseLayout = true  //Reverses list to start from the bottom
        ) {
            items(messageList.reversed()){  //Displays messages in reverse order
                MessageRow(messageModel = it)   //
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel){
    val isModel = messageModel.role=="model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                modifier = Modifier.align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if(isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ColorModelMessage else ColorUserMessage)
                    .padding(16.dp)
            ){
                SelectionContainer {     //Allows text selection in the message
                    Text(
                        text = messageModel.message,     //Displays message
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// MessageInput composable providing a text input and send button
@Composable
fun MessageInput(onMesssageSend: (String)-> Unit){
    var message by remember {
        mutableStateOf("") // stores the message typed by the user
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row (
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it   //updates message text as user types
            }
        )
        IconButton(onClick = {
            if (message.isNotEmpty()){
                onMesssageSend(message)
                message = ""  //clears input field after sending
            }
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun AppHeader(){
    Box (
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
    ){
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Gemichat",
            color = Color.White,
            fontSize = 22.sp
            )
    }
}

