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
        inputTitle2 = VariableManager.Instance.morningTitleText;
        inputContext2 = VariableManager.Instance.morningContextText;
        Type(inputTitle2, inputContext2);
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
