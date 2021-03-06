import request from '@/utils/request'
export function fetchList(params) {
	return request({
		url: '/merchat/merchatBusinessMaterials/list',
		method: 'get',
		params: params
	})
}
export function createMerchatBusinessMaterials(data) {
	return request({
		// url: '/merchat/merchatBusinessMaterials/create',
		url: '/merchat/merchatBusinessMaterials/applyForMerchant',
		method: 'post',
		data: data
	})
}

// export function queryByApplymentId(data) {
// 	return request({
// 		url: '/merchat/merchatBusinessMaterials/queryByApplymentId',
// 		method: 'post',
// 		data: data
// 	})
// }

export function queryByApplymentId(params) {
	return request({
		url: '/merchat/merchatBusinessMaterials/queryByApplymentId',
		method: 'get',
		params: params
	})
}

export function queryByBusinessCode(params) {
	return request({
		url: '/merchat/merchatBusinessMaterials/queryByBusinessCode',
		method: 'get',
		params: params
	})
}

export function deleteMerchatBusinessMaterials(id) {
	return request({
		url: '/merchat/merchatBusinessMaterials/delete/' + id,
		method: 'get',
	})
}

export function getMerchatBusinessMaterials(id) {
	return request({
		url: '/merchat/merchatBusinessMaterials/' + id,
		method: 'get',
	})
}

export function updateMerchatBusinessMaterials(id, data) {
	return request({
		url: '/merchat/merchatBusinessMaterials/update/' + id,
		method: 'post',
		data: data
	})
}
