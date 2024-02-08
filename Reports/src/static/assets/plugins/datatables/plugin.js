require.config({
    shim: {
        'datatables.net': ['jquery','core'],
        'datatables': ['jquery','core'],
      
        'jszip':['datatables'],
        'buttons': ['datatables'],
        'buttons-flash': ['datatables','buttons'],
        'buttons-html5': ['datatables','buttons'],
        
        'datatables.net-jszip':['datatables.net'],
        'datatables.net-buttons': ['datatables.net'],
        'datatables.net-buttons-flash': ['datatables.net','datatables.net-buttons'],
        'datatables.net-buttons-html5': ['datatables.net','datatables.net-buttons'],
       
    },
    paths: {
        'datatables.net': 'assets/plugins/datatables/DataTables-1.10.16/js/jquery.dataTables',
        'datatables': 'assets/plugins/datatables/DataTables-1.10.16/js/jquery.dataTables',
  
        'jszip':'assets/plugins/datatables/JSZip-2.5.0/jszip',
        'datatables.net-jszip':'assets/plugins/datatables/JSZip-2.5.0/jszip',
        
        'buttons':'assets/plugins/datatables/Buttons-1.6.1/js/dataTables.buttons',
        'datatables.net-buttons':'assets/plugins/datatables/Buttons-1.6.1/js/dataTables.buttons',
        'buttons-flash':'assets/plugins/datatables/Buttons-1.6.1/js/buttons.flash',
        'datatables.net-buttons-flash':'assets/plugins/datatables/Buttons-1.6.1/js/buttons.flash',
        'buttons-html5':'assets/plugins/datatables/Buttons-1.6.1/js/buttons.html5',
        'datatables.net-buttons-html5':'assets/plugins/datatables/Buttons-1.6.1/js/buttons.html5',
    }
    
  
    
    
    	
    
    
});