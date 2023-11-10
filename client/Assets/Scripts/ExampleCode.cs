using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class ExampleCode : MonoBehaviour, IPointerClickHandler
{
    GameObject player;
    // Start is called before the first frame update
    void Start()
    {
        player = GameObject.Find("Player");
    }

    public void OnPointerClick(PointerEventData eventData){
        player.GetComponent<CoroutineController>().AlertCoroutine();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
