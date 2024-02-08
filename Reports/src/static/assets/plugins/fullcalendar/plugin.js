require.config({
    shim: {
        'fullcalendar': ['moment', 'jquery','datepicker'],
    },
    paths: {
        'fullcalendar': 'assets/plugins/fullcalendar/js/fullcalendar.min',
        'moment': 'assets/plugins/fullcalendar/js/moment.min',
        'datepicker':'assets/plugins/fullcalendar/js/bootstrap-datepicker'
    }
});