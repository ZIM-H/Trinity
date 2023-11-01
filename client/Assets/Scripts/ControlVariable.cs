using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class ControlVariable : MonoBehaviour, IPointerClickHandler
{
    public GameObject beaker;
    public GameObject cube;
    
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void OnPointerClick(PointerEventData eventData){
        Debug.Log(beaker.GetComponent<TouchEvent>().condition);
        Debug.Log(cube.GetComponent<TouchEvent>().condition);
    }



}
