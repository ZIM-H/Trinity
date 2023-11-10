using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using UnityEngine;
using TMPro;
using System;

public class TextTyper : MonoBehaviour
{
    public string inputTitle1;
    public string inputContext1;
    public string inputTitle2;
    public string inputContext2;
    string inputString;
    TextMeshProUGUI titleText;
    TextMeshProUGUI contextText;

    char[] charArray;
    
    // Start is called before the first frame update
    void Start()
    {
        titleText = transform.GetChild(0).gameObject.GetComponent<TextMeshProUGUI>();
        contextText = transform.GetChild(1).gameObject.GetComponent<TextMeshProUGUI>();
        StartCoroutine(TypeTotal(6.0f, inputTitle1, inputContext1, inputTitle2, inputContext2));
    }

    IEnumerator TypeTotal(float time, string inputT1, string inputC1, string inputT2, string inputC2)
    {
        TypeInitializer();
        Type(inputT1, inputC1);
        yield return new WaitForSeconds(time);
        TypeInitializer();
        Type(inputT2, inputC2);
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
        
        StartCoroutine(Typing(1.0f/charArray.Length, titleText));
        StartCoroutine(ExecuteAfterTitleType(1.5f, contextInput));
        

    }
    public void TypeContext(string stringInput){
        inputString = stringInput;
        charArray = inputString.ToCharArray();
        
        StartCoroutine(Typing(1.5f/charArray.Length, contextText));

    }
    IEnumerator Typing(float time, TextMeshProUGUI targetText){
            foreach(char c in charArray){

            yield return new WaitForSeconds(time);
            targetText.text += c;
            }
    }
    IEnumerator ExecuteAfterTitleType(float time, string text){
        yield return new WaitForSeconds(time);
        TypeContext(text);
    }
}
