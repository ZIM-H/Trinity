using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CoroutineController : MonoBehaviour
{
    GameObject fertilizerAlert;
    GameObject alert;
    GameObject canvas;
    // Start is called before the first frame update
    void Start()
    {
        canvas = GameObject.Find("Canvas");
        alert = canvas.transform.Find("Alert").gameObject;
        fertilizerAlert = canvas.transform.Find("FertilizerAlert").gameObject;
    }
    public void AlertCoroutine(){
        StartCoroutine(AlertArise());
    }
    IEnumerator AlertArise(){
        alert.SetActive(true);
        yield return new WaitForSeconds(3.0f);
        alert.SetActive(false);
    }
    public void FertilizerAlertCoroutine(){
        StartCoroutine(FertilizerAlertArise());
    }
    IEnumerator FertilizerAlertArise(){
        fertilizerAlert.SetActive(true);
        yield return new WaitForSeconds(3.0f);
        fertilizerAlert.SetActive(false);
    }


    // Update is called once per frame
    void Update()
    {
        
    }
}
