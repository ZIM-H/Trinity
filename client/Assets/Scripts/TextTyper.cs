using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using UnityEngine;
using TMPro;
using System;
using System.Text;


public class TextTyper : MonoBehaviour
{
    public string inputTitle1;
    public string inputContext1;
    public string inputTitle2;
    public string inputContext2;
    public string inputFoodContext;
    public string inputMonsterContext;
    string inputString;
    TextMeshProUGUI titleText;
    TextMeshProUGUI contextText;

    char[] charArray;
    char[] charArrayWithColor;
    // char[] foodAmountTextArray;
    // char[] monsterTextArray;
    
    // Start is called before the first frame update
    void Start()
    {
        titleText = transform.GetChild(0).gameObject.GetComponent<TextMeshProUGUI>();
        contextText = transform.GetChild(1).gameObject.GetComponent<TextMeshProUGUI>();
        inputTitle1 = VariableManager.Instance.nightTitleText;
        inputContext1 = VariableManager.Instance.nightContextText;
        StartCoroutine(TypeTotal(6.0f));
    }

    IEnumerator TypeTotal(float time)
    {
        TypeInitializer();
        Type(inputTitle1, inputContext1);
        yield return new WaitForSeconds(time);
        TypeInitializer();
        if (!VariableManager.Instance.gameOver) {
            inputTitle2 = VariableManager.Instance.morningTitleText;
            inputContext2 = VariableManager.Instance.morningContextText;
            inputFoodContext = VariableManager.Instance.morningFoodAmountText;
            inputMonsterContext = VariableManager.Instance.morningMonsterText;
        } else {
            if (VariableManager.Instance.victory) {
                inputTitle2 = "승리했습니다!";
            } else {
                inputTitle2 = VariableManager.Instance.date.ToString() + "일차 사망";
            }
            inputContext2 = VariableManager.Instance.gameOverText;
        }
        TypeWithColor(inputTitle2, inputContext2,inputFoodContext,inputMonsterContext);
    }


    // Update is called once per frame
    void Update()
    {
        
    }
    public void TypeInitializer(){
        titleText.text = "";
        contextText.text = "";
    }

    public void Type(string stringInput, string contextInput){
        inputString = stringInput;
        charArray = inputString.ToCharArray();
        StartCoroutine(TypingSound(0,1.0f));
        StartCoroutine(Typing(1.0f/charArray.Length, titleText));
        StartCoroutine(TypingSound(1.5f,2.0f));
        StartCoroutine(ExecuteAfterTitleType(1.5f, contextInput, 2.0f));
        

    }

    public void TypeWithColor(string stringInput, string contextInput, string foodAmountText, string monsterText){
        inputString = stringInput;
        charArray = inputString.ToCharArray();
        // foodAmountTextArray = foodAmountText.ToCharArray();
        // monsterTextArray = monsterText.ToCharArray();
        
        StartCoroutine(TypingSound(0,1.0f));
        StartCoroutine(Typing(1.0f/charArray.Length, titleText));
        StartCoroutine(TypingSound(1.5f,1.5f));
        StartCoroutine(ExecuteAfterTitleType(1.5f, contextInput, 1.5f));
        
        StartCoroutine(TypingSound(3.5f,0.5f));
        StartCoroutine(ExecuteAfterTitleTypeWithColor(3.5f, foodAmountText, 0.5f, "green"));
        if (monsterText.Length > 0) {
            StartCoroutine(TypingSound(4.5f,0.5f));
            StartCoroutine(ExecuteAfterTitleTypeWithColor(4.5f, monsterText, 0.5f, "red"));
        }

    }
    public void TypeContext(string stringInput, float typingTime){
        inputString = stringInput;
        charArray = inputString.ToCharArray();
        
        StartCoroutine(Typing(typingTime/charArray.Length, contextText));

    }
     public void TypeContextWithColor(string stringInput, float typingTime, string color){
        
        StartCoroutine(TypingWithColor(typingTime/charArray.Length, contextText, color, stringInput));

    }
    IEnumerator Typing(float time, TextMeshProUGUI targetText){
        foreach(char c in charArray){
            yield return new WaitForSeconds(time);
            targetText.text += c;
        }
    }
    IEnumerator TypingWithColor(float time, TextMeshProUGUI targetText, string color, string stringInput)
    {
        foreach (char c in stringInput.ToCharArray())
        {
            yield return new WaitForSeconds(time);
            targetText.text += "<color=" + color + ">" + c + "</color>";
        }
        targetText.text += "\n";
    }

    IEnumerator ExecuteAfterTitleType(float time, string text, float typingTime){
        yield return new WaitForSeconds(time);
        TypeContext(text, typingTime);
    }
    IEnumerator ExecuteAfterTitleTypeWithColor(float time, string text, float typingTime, string color){
        yield return new WaitForSeconds(time);
        TypeContextWithColor(text, typingTime, color);
    }
    IEnumerator TypingSound(float time1, float time2){
        yield return new WaitForSeconds(time1);
        GetComponent<AudioSource>().Play();
        yield return new WaitForSeconds(time2);
        GetComponent<AudioSource>().Stop();
    }

}
