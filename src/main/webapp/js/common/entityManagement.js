/**
 * Common JavaScript functions for entity management
 */

// Configuration object to be set by concrete implementations
var entityConfig = {
    getListUrl: '',
    addUrl: '',
    updateUrl: '',
    deleteUrl: '',
    importUrl: '',
    exportUrl: ''
};

// Initialize the page
$(function() {
    initTable();
    initSearchHandler();
    initButtonHandlers();
});

// Initialize the entity table
function initTable() {
    $('#entityTable').bootstrapTable({
        url: entityConfig.getListUrl,
        method: 'get',
        queryParams: function(params) {
            return {
                offset: params.offset,
                limit: params.limit,
                searchType: $('#searchType').val(),
                keyWord: $('#keyword').val()
            };
        }
    });
}

// Initialize search functionality
function initSearchHandler() {
    $('#searchBtn').click(function() {
        $('#entityTable').bootstrapTable('refresh');
    });
}

// Initialize button handlers
function initButtonHandlers() {
    $('#addBtn').click(function() {
        showEntityModal();
    });
    
    $('#importBtn').click(function() {
        $('#importModal').modal('show');
    });
    
    $('#exportBtn').click(function() {
        exportEntities();
    });
}

// Show entity modal for add/edit
function showEntityModal(id) {
    $('#entityForm')[0].reset();
    if (id) {
        $.get(entityConfig.getListUrl, {
            searchType: 'searchByID',
            keyWord: id
        }, function(response) {
            if (response.result == 'success') {
                var entity = response.data;
                $('#id').val(entity.id);
                $('#name').val(entity.name);
                // Additional fields should be set by concrete implementations
            }
        });
    }
    $('#entityModal').modal('show');
}

// Save entity (create or update)
function saveEntity() {
    var formData = $('#entityForm').serialize();
    var url = $('#id').val() ? entityConfig.updateUrl : entityConfig.addUrl;
    
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        success: function(response) {
            if (response.result == 'success') {
                $('#entityModal').modal('hide');
                $('#entityTable').bootstrapTable('refresh');
            }
        }
    });
}

// Delete entity
function deleteEntity(id) {
    if (confirm('确认删除该记录？')) {
        $.get(entityConfig.deleteUrl, {
            id: id
        }, function(response) {
            if (response.result == 'success') {
                $('#entityTable').bootstrapTable('refresh');
            }
        });
    }
}

// Import entities
function importEntities() {
    var formData = new FormData($('#importForm')[0]);
    $.ajax({
        url: entityConfig.importUrl,
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if (response.result == 'success') {
                $('#importModal').modal('hide');
                $('#entityTable').bootstrapTable('refresh');
            }
        }
    });
}

// Export entities
function exportEntities() {
    window.location.href = entityConfig.exportUrl + '?' + $.param({
        searchType: $('#searchType').val(),
        keyWord: $('#keyword').val()
    });
}
