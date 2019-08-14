const mealAjaxURL = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    makeEditable({
            ajaxUrl: mealAjaxURL,
            datatableApi: $("#datatable").DataTable({
                "ajax": {
                    "url": mealAjaxURL,
                    "dataSrc": ""
                },
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime",
                        "render" : function (date, type, row) {
                            if (type==="display") {
                                var str = date.replace("T", " ");
                                return str.substring(0, 16);
                            }
                            return date;
                        }
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "orderable": false,
                        "defaultContent": "",
                        "render": renderEditBtn
                    },
                    {
                        "orderable": false,
                        "defaultContent": "",
                        "render": renderDeleteBtn
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ],
                "createdRow" : function (row, data, dataIndex) {
                    // $(row).attr("color:blue");
                    if (data.excess) {
                        $(row).attr("data-mealExcess", true);
                    } else {
                        $(row).attr("data-mealExcess", false);
                    }
                }
            }),
        updateTable: updateFilteredTable
        }
    );
});